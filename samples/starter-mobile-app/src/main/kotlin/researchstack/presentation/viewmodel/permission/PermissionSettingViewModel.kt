package researchstack.presentation.viewmodel.permission

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import researchstack.data.datasource.local.room.dao.FileUploadRequestDao
import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.Study
import researchstack.domain.model.StudyStatusModel
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.usecase.shareagreement.GetShareAgreementUseCase
import researchstack.domain.usecase.shareagreement.UpdateShareAgreementUseCase
import researchstack.domain.usecase.study.GetStudyByIdUseCase
import researchstack.domain.usecase.wearable.GetShareAgreementFromDataTypeUseCase
import researchstack.domain.usecase.wearable.PassiveDataStatusUseCase
import researchstack.domain.usecase.wearable.WearablePassiveDataStatusSenderUseCase
import javax.inject.Inject

@HiltViewModel
class PermissionSettingViewModel
@Inject
constructor(
    private val getStudyByIdUseCase: GetStudyByIdUseCase,
    private val getShareAgreementUseCase: GetShareAgreementUseCase,
    private val updateShareAgreementUseCase: UpdateShareAgreementUseCase,
    private val fileUploadRequestDao: FileUploadRequestDao,
    private val passiveDataStatusUseCase: PassiveDataStatusUseCase,
    private val getShareAgreementFromDataTypeUseCase: GetShareAgreementFromDataTypeUseCase,
    private val wearablePassiveDataStatusSenderUseCase: WearablePassiveDataStatusSenderUseCase,
) : ViewModel() {
    private val _study = MutableStateFlow<Study?>(null)
    val study: StateFlow<Study?> = _study

    private val _shareAgreements = MutableStateFlow<List<ShareAgreement>>(emptyList())
    val shareAgreements: StateFlow<List<ShareAgreement>> = _shareAgreements

    private val _remainFileCount = MutableStateFlow<Int>(-1)
    val remainFileCount: StateFlow<Int> = _remainFileCount

    fun getStudyAndPermissions(studyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getStudyByIdUseCase(studyId).collect {
                _study.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            getShareAgreementUseCase(studyId).collect {
                _shareAgreements.value = it
            }
        }
    }

    fun updateShareAgreement(shareAgreement: ShareAgreement) {
        viewModelScope.launch(Dispatchers.IO) {
            updateShareAgreementUseCase(shareAgreement)
        }
    }

    fun getRemainUploadRequest(studyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fileUploadRequestDao.findAllByStudyId(studyId).collect {
                _remainFileCount.value = it.size
            }
        }
    }

    fun sendPassiveDataStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            shareAgreements.value.filter {
                it.dataType is PrivDataType
            }.forEach {
                if ((it.dataType as PrivDataType).isPassive) {
                    Log.i(PermissionSettingViewModel::class.java.simpleName, "passive data $it")
                    val listData =
                        getShareAgreementFromDataTypeUseCase(it.dataType.name).first().filter { shareAgreement ->
                            (
                                getStudyByIdUseCase(shareAgreement.studyId).first().status
                                    ?: StudyStatusModel.STUDY_STATUS_PARTICIPATING
                                ) == StudyStatusModel.STUDY_STATUS_PARTICIPATING
                        }
                    if (passiveDataStatusUseCase(
                            it.dataType,
                            listData.isNotEmpty()
                        )
                    ) {
                        Log.i(
                            PermissionSettingViewModel::class.java.simpleName,
                            "passive data " + it.dataType.name
                        )
                        wearablePassiveDataStatusSenderUseCase.invoke(
                            it.dataType,
                            listData.isNotEmpty(),
                        ).onFailure { throwE ->
                            Log.e(
                                PermissionSettingViewModel::class.java.name,
                                throwE.message.toString()
                            )
                        }
                    }
                }
            }
        }

        fun updateShareAgreement(shareAgreement: ShareAgreement) {
            viewModelScope.launch(Dispatchers.IO) {
                updateShareAgreementUseCase(shareAgreement)
            }
        }

        fun getRemainUploadRequest(studyId: String) {
            viewModelScope.launch(Dispatchers.IO) {
                fileUploadRequestDao.findAllByStudyId(studyId).collect {
                    _remainFileCount.value = it.size
                }
            }
        }

        fun sendPassiveDataStatus() {
            CoroutineScope(Dispatchers.IO).launch {
                shareAgreements.value
                    .filter {
                        it.dataType is PrivDataType
                    }.forEach {
                        if ((it.dataType as PrivDataType).isPassive) {
                            Log.i(PermissionSettingViewModel::class.java.simpleName, "passive data $it")
                            val listData =
                                getShareAgreementFromDataTypeUseCase(it.dataType.name).first()
                                    .filter { shareAgreement ->
                                        (
                                            getStudyByIdUseCase(shareAgreement.studyId).first().status
                                                ?: StudyStatusModel.STUDY_STATUS_PARTICIPATING
                                            ) == StudyStatusModel.STUDY_STATUS_PARTICIPATING
                                    }
                            if (passiveDataStatusUseCase(
                                    it.dataType,
                                    listData.isNotEmpty(),
                                )
                            ) {
                                Log.i(
                                    PermissionSettingViewModel::class.java.simpleName,
                                    "passive data " + it.dataType.name,
                                )
                                wearablePassiveDataStatusSenderUseCase
                                    .invoke(
                                        it.dataType,
                                        listData.isNotEmpty(),
                                    ).onFailure { throwE ->
                                        Log.e(
                                            PermissionSettingViewModel::class.java.name,
                                            throwE.message.toString(),
                                        )
                                    }
                            }
                        }
                    }
            }
        }
    }
}
