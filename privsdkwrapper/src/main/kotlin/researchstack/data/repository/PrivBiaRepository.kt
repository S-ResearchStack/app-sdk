package researchstack.data.repository

import android.util.Log
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.TrackerUserProfile
import com.samsung.android.service.health.tracking.data.ValueKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.UserProfile
import researchstack.domain.model.priv.Bia
import researchstack.domain.repository.WearableDataRepository
import researchstack.requiredinterface.UserProfileAccessible
import java.util.Calendar
import javax.inject.Inject

class PrivBiaRepository @Inject constructor(
    private val userProfileAccessible: UserProfileAccessible,
    override val fileRepository: FileRepository<Bia>,
) : WearableDataRepository<Bia>, PrivRepository<Bia>() {
    private var userProfile: UserProfile? = null
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.BIA

    init {
        CoroutineScope(Dispatchers.IO).launch {
            userProfileAccessible.getUserProfile().collect {
                Log.i(TAG, "userProfile init")
                userProfile = it
            }
        }
    }

    override val healthDataTracker: HealthTracker by lazy {
        Log.i(TAG, "healthDataTracker init")
        userProfile?.let {
            Log.i(TAG, "healthDataTracker init")
            PrivDataRequester.healthTrackingService.getHealthTracker(
                healthTrackerType,
                TrackerUserProfile.Builder()
                    .setGender(it.gender.ordinal)
                    .setWeight(it.weight)
                    .setHeight(it.height)
                    .setAge(Calendar.getInstance().get(Calendar.YEAR) - it.yearBirth)
                    .build()
            )
        } ?: PrivDataRequester.healthTrackingService.getHealthTracker(healthTrackerType)
    }

    override fun receiveDataFlow() = receiveDataPoints().flatten().map { it.toModel() }

    private fun DataPoint.toModel() = Bia(
        timestamp,
        getValue(ValueKey.BiaSet.BASAL_METABOLIC_RATE),
        getValue(ValueKey.BiaSet.BODY_FAT_MASS),
        getValue(ValueKey.BiaSet.BODY_FAT_RATIO),
        getValue(ValueKey.BiaSet.FAT_FREE_MASS),
        getValue(ValueKey.BiaSet.FAT_FREE_RATIO),
        getValue(ValueKey.BiaSet.SKELETAL_MUSCLE_MASS),
        getValue(ValueKey.BiaSet.SKELETAL_MUSCLE_RATIO),
        getValue(ValueKey.BiaSet.TOTAL_BODY_WATER),
        getValue(ValueKey.BiaSet.PROGRESS),
        getValue(ValueKey.BiaSet.STATUS)
    )

    companion object {
        private val TAG = PrivBiaRepository::class.simpleName
    }
}
