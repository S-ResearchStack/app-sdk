package researchstack.data.datasource.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.datasource.local.pref.SyncTimePref
import researchstack.data.datasource.local.room.dao.TimestampEntityBaseDao
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.HealthDataModel
import kotlin.math.pow
import kotlin.math.sqrt

abstract class LocationTracker(
    val context: Context,
    dataStore: DataStore<Preferences>,
    override val timestampEntityBaseDao: TimestampEntityBaseDao<Speed>,
    override val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
) : BaseTracker<Speed>(dataStore) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback = MyLocationCallback()
    override val syncTimePrefKey = SyncTimePref.SyncTimePrefKey.SPEED_SYNC
    override val trackerDataType = TrackerDataType.SPEED

    private fun isLocationPermissionGranted(): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    override fun startTracking(): Flow<Speed> {
        if (isTracking) return dataFlow
        isTracking = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest =
            LocationRequest.create().apply {
                interval = INTERVAL
                maxWaitTime = MAX_WAIT_TIME
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

        dataFlow =
            callbackFlow {
                locationCallback.mProducerScope = this
                if (isLocationPermissionGranted()) {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper(),
                    )
                }
                sendChannel = channel
                awaitClose {
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }
            }.flowOn(Dispatchers.IO)

        return dataFlow
    }

    companion object {
        private const val INTERVAL = 600000L
        private const val MAX_WAIT_TIME = 60000L
        private val TAG = this::class.simpleName
    }

    class MyLocationCallback : LocationCallback() {
        var mLastLocation: Location? = null
        lateinit var mSpeed: Speed
        lateinit var mProducerScope: ProducerScope<Speed>

        private fun calculateSpeed(
            from: Location,
            to: Location,
        ): Float {
            val powX = (to.longitude - from.longitude).pow(2.0)
            val powY = (to.latitude - from.latitude).pow(2.0)
            val distant = sqrt(powX + powY)
            val deltaTime = (to.time - from.time)
            return (distant / deltaTime).toFloat()
        }

        override fun onLocationResult(currentLocationResult: LocationResult) {
            val lastLocation = currentLocationResult.lastLocation
            if (mLastLocation == null) {
                mLastLocation = lastLocation
                return
            }
            var speed = 0f

            if (lastLocation.hasSpeed()) {
                speed = lastLocation.speed
            } else {
                mLastLocation?.let { location ->
                    speed = calculateSpeed(location, lastLocation)
                }
            }
            mSpeed = Speed(speed, mLastLocation!!.time, lastLocation.time)

            mLastLocation = lastLocation
            mProducerScope.trySend(mSpeed)
        }
    }
}
