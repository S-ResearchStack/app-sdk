package researchstack.wearable.standalone.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.domain.model.priv.PrivDataType
import researchstack.wearable.standalone.BuildConfig
import researchstack.wearable.standalone.data.datasource.grpc.mapper.toDomain
import researchstack.wearable.standalone.domain.model.ShareAgreement
import researchstack.wearable.standalone.domain.model.UserProfileModel
import researchstack.wearable.standalone.domain.usecase.PassiveDataStatusUseCase
import researchstack.wearable.standalone.domain.usecase.auth.CheckSignInUseCase
import researchstack.wearable.standalone.domain.usecase.auth.SignUpUseCase
import researchstack.wearable.standalone.domain.usecase.profile.RegisterProfileUseCase
import researchstack.wearable.standalone.domain.usecase.shareagreement.SaveShareAgreementUseCase
import researchstack.wearable.standalone.domain.usecase.study.StudyJoinUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SplashLoadingViewModel @Inject constructor(
    private val checkSignInUseCase: CheckSignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val registerProfileUseCase: RegisterProfileUseCase,
    private val studyJoinUseCase: StudyJoinUseCase,
    private val saveShareAgreementUseCase: SaveShareAgreementUseCase,
    private val studyOutPort: StudyOutPort,
    private val passiveDataStatusUseCase: PassiveDataStatusUseCase,
) : ViewModel() {
    private var _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    private val anonymousUser = UserProfileModel(
        firstName = "",
        lastName = "",
        birthday = LocalDate.now(),
        email = "",
        phoneNumber = "",
        address = ""
    )

    private fun createShareAgreements(
        studyId: String,
        privDataTypes: List<PrivDataType>,
    ): MutableList<ShareAgreement> {
        val shareAgreements =
            mutableListOf<ShareAgreement>().apply {
                for (dataType in privDataTypes) {
                    add(ShareAgreement(studyId, dataType, approval = true))
                }
            }
        return shareAgreements
    }

    fun createAccount() {
        viewModelScope.launch {
            if (!checkSignInUseCase().getOrDefault(false)) {
                Log.i(TAG, "try to create an account")
                val studyId = BuildConfig.STUDY_ID
                signUpUseCase().getOrThrow()
                registerProfileUseCase(anonymousUser).getOrThrow()
                studyJoinUseCase(studyId).getOrThrow()
                studyOutPort.getParticipationRequirementList(studyId)
                    .map {
                        val dataTypes = it.toDomain().privDataTypes
                        Log.i(TAG, "collecting data types: $dataTypes")
                        saveShareAgreementUseCase.save(
                            createShareAgreements(studyId, dataTypes)
                        )
                        dataTypes.forEach {
                            when (it) {
                                PrivDataType.WEAR_ACCELEROMETER ->
                                    passiveDataStatusUseCase(PrivDataType.WEAR_ACCELEROMETER, true)
                                PrivDataType.WEAR_HEART_RATE ->
                                    passiveDataStatusUseCase(PrivDataType.WEAR_HEART_RATE, true)
                                PrivDataType.WEAR_PPG_GREEN ->
                                    passiveDataStatusUseCase(PrivDataType.WEAR_PPG_GREEN, true)
                                else -> {}
                            }
                        }
                    }.getOrThrow()
            } else {
                Log.i(TAG, "already has an account")
            }
            _isReady.value = true
        }
    }

    companion object {
        private val TAG = SplashLoadingViewModel::class.simpleName
    }
}
