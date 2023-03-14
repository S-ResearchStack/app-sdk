package healthstack.backend.integration.adapter

import healthstack.healthdata.link.HealthData
import okhttp3.internal.toImmutableMap

fun HealthData.instantToString(): HealthData {

    val convertedHealthData = data.map {
        val mutableMap = it.toMutableMap()

        if (it.containsKey(HealthData.TIME_KEY)) {
            mutableMap[HealthData.TIME_KEY] = it[HealthData.TIME_KEY].toString()
        } else {
            mutableMap[HealthData.START_TIME_KEY] = it[HealthData.START_TIME_KEY].toString()
            mutableMap[HealthData.END_TIME_KEY] = it[HealthData.END_TIME_KEY].toString()
        }

        mutableMap.toImmutableMap()
    }

    return HealthData(type, convertedHealthData)
}
