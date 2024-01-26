package healthstack.healthdata.link.healthconnect

import android.annotation.SuppressLint
import android.os.RemoteException
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter.Companion
import healthstack.healthdata.link.Change
import healthstack.healthdata.link.HealthData
import healthstack.healthdata.link.HealthDataLink
import java.time.Instant
import kotlin.reflect.KClass

class HealthConnectAdapter(
    healthDataTypeNames: List<String>,
    private val healthConnectClient: HealthConnectClient,
) : HealthDataLink {
    private val healthDataTypes: List<KClass<out Record>> = healthDataTypeNames.map {
        HealthConnectUtils.nameToRecord(it)
    }

    private val requiredPermissions: Set<String> = healthDataTypes.map {
        listOf(
            HealthPermission.getReadPermission(it),
            HealthPermission.getWritePermission(it)
        )
    }
        .flatten()
        .toSet()

    private lateinit var launcher: ActivityResultLauncher<Set<String>>

    fun createLauncher(context: ComponentActivity) {
        launcher = context.registerForActivityResult(
            PermissionController.createRequestPermissionResultContract()
        ) { granted ->
            if (granted.containsAll(requiredPermissions)) return@registerForActivityResult
        }
    }

    override suspend fun hasAllPermissions(): Boolean =
        requiredPermissions == healthConnectClient.permissionController.getGrantedPermissions()

    override suspend fun requestPermissions() {
        healthConnectClient.permissionController.revokeAllPermissions()
        launcher.launch(requiredPermissions)
    }

    override suspend fun getHealthData(startTime: Instant, endTime: Instant, healthDataTypeName: String): HealthData {
        require(endTime.isAfter(startTime))

        // TODO: check permission

        val recordType = HealthConnectUtils.nameToRecord(healthDataTypeName)

        return try {
            val recordsResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType,
                    Companion.between(startTime, endTime)
                )
            )

            recordsResponse.toHealthData(healthDataTypeName)
        } catch (e: RemoteException) {
            Log.e(HealthConnectAdapter::class.simpleName, e.message.toString())
            HealthData(healthDataTypeName, emptyList())
        }
    }

    override suspend fun getChangesToken(healthDataTypeName: String): String {
        val recordType = HealthConnectUtils.nameToRecord(healthDataTypeName)

        return healthConnectClient.getChangesToken(
            ChangesTokenRequest(setOf(recordType))
        )
    }

    override suspend fun getChanges(token: String, healthDataTypeName: String): Change {
        HealthConnectUtils.nameToRecord(healthDataTypeName)
        val changesResponse = healthConnectClient.getChanges(token)

        return changesResponse.toChange(healthDataTypeName)
    }

    override fun isIntervalData(healthDataName: String): Boolean {
        TODO("Not yet implemented")
    }
}
