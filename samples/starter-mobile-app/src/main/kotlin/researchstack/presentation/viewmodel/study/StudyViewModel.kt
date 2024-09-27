package researchstack.presentation.viewmodel.study

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.R
import researchstack.data.datasource.local.pref.SyncTimePref
import researchstack.data.datasource.local.pref.SyncTimePref.SyncTimePrefKey.CALL_LOG_SYNC
import researchstack.data.datasource.local.pref.SyncTimePref.SyncTimePrefKey.PLACE_EVENT_SYNC
import researchstack.data.datasource.local.pref.SyncTimePref.SyncTimePrefKey.PRESENCE_EVENT_SYNC
import researchstack.data.datasource.local.pref.SyncTimePref.SyncTimePrefKey.USAGE_STATS_SAVE
import researchstack.data.datasource.local.pref.dataStore
import researchstack.domain.exception.AlreadyJoinedStudy
import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.Study
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.usecase.shareagreement.ShareAgreementUseCase
import researchstack.domain.usecase.study.StudyJoinUseCase
import researchstack.domain.usecase.wearable.PassiveDataStatusUseCase
import researchstack.domain.usecase.wearable.WearablePassiveDataStatusSenderUseCase
import researchstack.presentation.service.TrackerDataForegroundService
import researchstack.presentation.worker.FetchStudyTasksWorker
import researchstack.presentation.worker.FetchStudyTasksWorker.Companion.STUDY_ID_KEY
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class StudyViewModel
@Inject
constructor(
    application: Application,
    private val studyShardViewModel: SharedStudyJoinViewModel,
    private val studyJoinUseCase: StudyJoinUseCase,
    private val shareAgreementUseCase: ShareAgreementUseCase,
    private val passiveDataStatusUseCase: PassiveDataStatusUseCase,
    private val wearablePassiveDataStatusUseCase: WearablePassiveDataStatusSenderUseCase,
) : AndroidViewModel(application) {
    val study: StateFlow<Study?> = studyShardViewModel.study

    private val _joinState = MutableStateFlow<StudyJoinState>(NotStarted)
    val joinState: StateFlow<StudyJoinState> = _joinState

    fun joinStudy(
        studyId: String,
    ) {
        _joinState.value = Joining
        viewModelScope.launch(Dispatchers.IO) {
            studyJoinUseCase(
                studyId,
                studyShardViewModel.eligibilityTestResult,
            ).onSuccess {
                // TODO how to handle
                onJoinStudy(studyId)
            }.onFailure { ex ->
                val message =
                    if (ex is AlreadyJoinedStudy) {
                        onJoinStudy(studyId)
                        getApplication<Application>().applicationContext.getString(R.string.already_joined_study)
                    } else {
                        "Something Wrong"
                    }

                _joinState.value = Fail(message)
            }
        }
    }

    private suspend fun onJoinStudy(
        studyId: String,
    ) {
        setSyncTimestamp()
        fetchStudyTasksWithWorker(studyId)

        studyShardViewModel.participationRequirement.value?.apply {
            val shareAgreements =
                createShareAgreements(
                    studyId,
                    trackerDataTypes,
                    privDataTypes,
                    deviceStatDataTypes,
                )

            if (shareAgreements.isNotEmpty()) {
                shareAgreementUseCase.save(shareAgreements).onSuccess {
                    shareAgreements.filter { it.dataType is PrivDataType }.forEach {
                        if ((it.dataType as PrivDataType).isPassive && it.approval) {
                            if (passiveDataStatusUseCase(it.dataType, true)) {
                                wearablePassiveDataStatusUseCase(PrivDataType.valueOf(it.dataType.name), true)
                            }
                        }
                    }
                }
            }

            if (trackerDataTypes.isNotEmpty()) {
                startSensorForegroundService()
            }
        }

        _joinState.value = Success
    }

    private suspend fun setSyncTimestamp() {
        // FIXME remove skip call and we should consider multi-study
        val timestamp = Instant.now().toEpochMilli()
        listOf(USAGE_STATS_SAVE, PLACE_EVENT_SYNC, PRESENCE_EVENT_SYNC, CALL_LOG_SYNC)
            .forEach {
                SyncTimePref(getApplication<Application>().applicationContext.dataStore, it)
                    .update(timestamp)
            }
    }

    private fun fetchStudyTasksWithWorker(studyId: String) {
        val studyIdData =
            Data
                .Builder()
                .putString(STUDY_ID_KEY, studyId)
                .build()

        WorkManager
            .getInstance(getApplication<Application>().applicationContext)
            .enqueue(
                OneTimeWorkRequestBuilder<FetchStudyTasksWorker>()
                    .setInputData(studyIdData)
                    .build(),
            )
    }

    private fun createShareAgreements(
        studyId: String,
        trackerDataTypes: List<TrackerDataType>,
        privDataTypes: List<PrivDataType>,
        deviceStatDataTypes: List<DeviceStatDataType>,
    ): MutableList<ShareAgreement> {
        val shareAgreements =
            mutableListOf<ShareAgreement>().apply {
                for (
                dataType in listOf(
                    trackerDataTypes,
                    privDataTypes,
                    deviceStatDataTypes,
                ).flatten()
                ) {
                    add(ShareAgreement(studyId, dataType, approval = true))
                }
            }
        return shareAgreements
    }

    private fun startSensorForegroundService() {
        getApplication<Application>().applicationContext.startForegroundService(
            Intent(getApplication<Application>().applicationContext, TrackerDataForegroundService::class.java),
        )
    }

    sealed class StudyJoinState

    object NotStarted : StudyJoinState()

    object Joining : StudyJoinState()

    object Success : StudyJoinState()

    class Fail(
        val message: String,
    ) : StudyJoinState()
}
