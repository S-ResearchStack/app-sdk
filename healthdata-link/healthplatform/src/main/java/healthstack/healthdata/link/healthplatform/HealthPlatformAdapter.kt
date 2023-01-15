package healthstack.healthdata.link.healthplatform

import androidx.concurrent.futures.await
import com.google.android.libraries.healthdata.HealthDataClient
import com.google.android.libraries.healthdata.data.DataType
import com.google.android.libraries.healthdata.data.IntervalDataType
import com.google.android.libraries.healthdata.data.IntervalDataTypes
import com.google.android.libraries.healthdata.data.IntervalReadSpec
import com.google.android.libraries.healthdata.data.ReadDataRequest
import com.google.android.libraries.healthdata.data.SampleDataType
import com.google.android.libraries.healthdata.data.SampleDataTypes
import com.google.android.libraries.healthdata.data.SampleReadSpec
import com.google.android.libraries.healthdata.data.TimeSpec
import com.google.android.libraries.healthdata.permission.AccessType
import com.google.android.libraries.healthdata.permission.Permission
import healthstack.healthdata.link.Change
import healthstack.healthdata.link.HealthData
import healthstack.healthdata.link.HealthDataLink
import java.time.Instant

class HealthPlatformAdapter(
    private val healthDataClient: HealthDataClient,
    healthDataTypeNames: List<String>,
) : HealthDataLink {
    companion object {
        private val allSampleDataNames: Set<String> =
            SampleDataTypes.getAllDataTypes().map {
                it.name
            }.toHashSet()

        private val allIntervalDataNames: Set<String> =
            IntervalDataTypes.getAllDataTypes().map {
                it.name
            }.toHashSet()

        fun convertStringToHealthDataType(healthDataTypeString: String) =
            when (healthDataTypeString) {
                in allSampleDataNames -> SampleDataTypes.fromName(healthDataTypeString)
                in allIntervalDataNames -> IntervalDataTypes.fromName(healthDataTypeString)
                else -> throw IllegalArgumentException("Cannot find dataType with given string.")
            }
    }

    private val healthDataTypes: List<DataType> = healthDataTypeNames.map {
        convertStringToHealthDataType(it)
    }

    private val requiredPermissions: Set<Permission> = healthDataTypes.flatMap {
        listOf(
            Permission.create(it, AccessType.READ),
            Permission.create(it, AccessType.WRITE)
        )
    }.toHashSet()

    suspend fun hasPermissions(permissions: Set<Permission>): Boolean {
        val grantedPermissions = healthDataClient.getGrantedPermissions(permissions).await()
        return grantedPermissions.containsAll(permissions)
    }

    override suspend fun hasAllPermissions(): Boolean {
        val grantedPermissions = healthDataClient.getGrantedPermissions(requiredPermissions).await()
        return grantedPermissions.containsAll(requiredPermissions)
    }

    override suspend fun requestPermissions() {
        if (hasAllPermissions())
            return

        healthDataClient.requestPermissions(requiredPermissions).await()
    }

    override suspend fun getHealthData(
        startTime: Instant,
        endTime: Instant,
        healthDataTypeName: String,
    ): HealthData {
        require(endTime.isAfter(startTime))

        val healthDataType = convertStringToHealthDataType(healthDataTypeName)
        val permissionSet = setOf(Permission.create(healthDataType, AccessType.READ))

        if (!hasPermissions(permissionSet))
            throw IllegalStateException("Required permissions are not granted.")

        val request = ReadDataRequest.builder().also {
            it.setTimeSpec(
                // TODO: get date from device DB
                TimeSpec.builder()
                    .setStartTime(startTime)
                    .setEndTime(endTime)
                    .build()
            )
            when (healthDataType) {
                is SampleDataType ->
                    it.addSampleReadSpec(
                        SampleReadSpec.builder(healthDataType).build()
                    )

                is IntervalDataType ->
                    it.addIntervalReadSpec(
                        IntervalReadSpec.builder(healthDataType).build()
                    )

                else -> throw IllegalArgumentException("Given dataType is not supported.")
            }
        }.build()

        return healthDataClient.readData(request).await().toHealthData(healthDataType)
    }

    override suspend fun getChangesToken(healthDataTypeName: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getChanges(token: String, healthDataTypeName: String): Change {
        TODO("Not yet implemented")
    }

    override fun isIntervalData(healthDataName: String): Boolean =
        healthDataName in allIntervalDataNames
}
