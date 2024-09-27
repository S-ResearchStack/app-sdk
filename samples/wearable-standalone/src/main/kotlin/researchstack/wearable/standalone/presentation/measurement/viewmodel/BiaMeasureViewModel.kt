package researchstack.wearable.standalone.presentation.measurement.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import researchstack.domain.model.Gender
import researchstack.domain.model.UserProfile
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.PrivDataType
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.domain.usecase.TrackDataUseCase
import researchstack.wearable.standalone.domain.usecase.TrackMeasureTimeUseCase
import researchstack.wearable.standalone.domain.usecase.UserProfileUseCase
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.main.screen.getItemPrefKey
import researchstack.wearable.standalone.presentation.measurement.screen.AskProfilePage
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.ErrorState
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.kgToLbs
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class BiaMeasureViewModel @Inject constructor(
    application: Application,
    private val trackMeasureTimeUseCase: TrackMeasureTimeUseCase,
    private val trackDataUseCase: TrackDataUseCase,
    private val userProfileUseCase: UserProfileUseCase,
) : AndroidViewModel(application) {

    private val _measureState = MutableLiveData<MeasureState>(None)
    val measureState: LiveData<MeasureState> = _measureState

    private val _progress = MutableLiveData("0")
    val progress: LiveData<String> = _progress

    private val _result = MutableLiveData(emptyMap<Int, String>())
    val result: LiveData<Map<Int, String>> = _result

    private val _isMetric = MutableLiveData(true)
    val isMetric: LiveData<Boolean> = _isMetric

    var profile: UserProfile? = null

    private val _failStatus = MutableLiveData(0)
    val failStatus: LiveData<Int> = _failStatus

    private var errorState: ErrorState = ErrorState.Normal
    private var startErrorTime = 0L

    fun postMeasureState(state: MeasureState) =
        _measureState.postValue(state)

    init {
        getProfile()
    }

    fun getProfile() {
        Log.i(TAG, "init")
        viewModelScope.launch {
            userProfileUseCase().collect {
                profile = it
                Log.i(TAG, "profile: $profile")
                _measureState.postValue(
                    if (profile == null) {
                        RequestProfile(AskProfilePage.values().toList())
                    } else {
                        RequestProfile(profile.toListUnsetPage())
                    }
                )
                cancel()
            }
        }
    }

    private fun UserProfile?.toListUnsetPage(): List<AskProfilePage> {
        val listUnsetPage = ArrayList<AskProfilePage>()
        this?.let {
            _isMetric.value = (isMetricUnit ?: true)
            Log.i(TAG, "else: ${_isMetric.value}")

            if (isMetricUnit == null) {
                listUnsetPage.add(AskProfilePage.MEASUREMENT_UNIT)
            }

            if (gender == Gender.UNKNOWN) {
                listUnsetPage.add(AskProfilePage.GENDER)
            }

            if (yearBirth <= 0) {
                listUnsetPage.add(AskProfilePage.AGE)
            }

            if (height <= 0f) {
                listUnsetPage.add(AskProfilePage.HEIGHT)
            }
        }
        listUnsetPage.add(AskProfilePage.WEIGHT)
        return listUnsetPage
    }

    fun setDefaultUnit(value: Boolean) {
        _isMetric.postValue(value)
    }

    fun stopTracking() =
        runBlocking {
            trackDataUseCase.stopTracking(PrivDataType.WEAR_BIA)
        }

    fun startTracking() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "start tracking for BIA")
            _measureState.postValue(None)
            delay(2000)
            errorState = errorState.resetState()
            trackDataUseCase.startTracking(PrivDataType.WEAR_BIA).collect {
                val bia = it as Bia
                Log.i(TAG, "progress: ${bia.measurementProgress} status: ${bia.status}")
                _progress.postValue(bia.measurementProgress.times(100).toInt().toString())
                if (isFailure(bia.status)) {
                    _failStatus.postValue(bia.status)
                    failureHandler()
                } else {
                    errorState = errorState.resetState()
                    _measureState.postValue(Measuring)
                    if (bia.measurementProgress == 1.0f) {
                        stopTracking()
                        _result.postValue(toMap(bia, profile!!))
                        _measureState.postValue(Completed)
                        trackMeasureTimeUseCase(
                            HomeScreenItem.BODY_COMPOSITION.getItemPrefKey(),
                            bia.timestamp
                        )
                        trackDataUseCase.sendData(bia, PrivDataType.WEAR_BIA)
                    }
                }
            }
        }
    }

    private fun failureHandler() {
        errorState = errorState.nextState()
        if (errorState.isFirstError()) {
            startErrorTime = System.currentTimeMillis()
        }
        if (System.currentTimeMillis() - startErrorTime >= BIA_ERROR_DURATION) {
            _measureState.postValue(Fail)
            stopTracking()
        }
    }

    private fun toMap(bia: Bia, profile: UserProfile): Map<Int, String> {
        var isMetric = true
        _isMetric.value?.let {
            isMetric = it
        }
        val context = getApplication<Application>().applicationContext

        val weightUnit = if (isMetric)
            context.resources.getString(R.string.bia_metric_weight_unit)
        else
            context.resources.getString(R.string.bia_imperial_weight_unit)

        return mapOf(
            R.string.bia_weight to "%.1f$weightUnit".format(profile.weight.kgToLbs(isMetric)),
            R.string.bia_skeletal_muscle to "%.1f$weightUnit".format(
                bia.skeletalMuscleMass.kgToLbs(
                    isMetric
                )
            ),
            R.string.bia_fat_mass to "%.1f$weightUnit".format(bia.bodyFatMass.kgToLbs(isMetric)),
            R.string.bia_body_fat to "%.1f%s".format(bia.bodyFatRatio, "%"),
            R.string.bia_bmi to "%.1f".format(profile.getBmi()),
            R.string.bia_body_water to "%.1f$weightUnit".format(
                bia.totalBodyWater.kgToLbs(
                    isMetric
                )
            ),
            R.string.bia_bmr to "%,dCal".format(bia.basalMetabolicRate.toInt()),
        )
    }

    private fun UserProfile.getBmi(): Double = weight / ((height / 100.0).pow(2))

    private fun isFailure(biaStatus: Int): Boolean = SET_ERROR_STATUS.contains(biaStatus)

    fun setUserProfile(userProfile: UserProfile) {
        profile = userProfile
        Log.i(TAG, "setUserProfile: $profile")
        profile?.let {
            viewModelScope.launch(Dispatchers.IO) {
                userProfileUseCase(it)
            }
        }
    }

    sealed class MeasureState
    object None : MeasureState()
    class RequestProfile(val listUnsetPage: List<AskProfilePage>) : MeasureState()
    object Help : MeasureState()
    object Measuring : MeasureState()
    object Completed : MeasureState()

    object Fail : MeasureState()

    companion object {
        private val TAG = BiaMeasureViewModel::class.simpleName
        const val BIA_ERROR_DURATION: Long = 3000
        private val SET_ERROR_STATUS = setOf(2, 4, 7, 8, 9, 10, 11, 13, 14, 15, 17, 18)
    }
}
