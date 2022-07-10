package com.samsung.healthcare.kit.app

import com.google.android.libraries.healthdata.data.DataType
import com.google.android.libraries.healthdata.data.IntervalDataTypes.SLEEP_SESSION
import com.google.android.libraries.healthdata.data.SampleDataType
import com.google.android.libraries.healthdata.data.SampleDataTypes.HEART_RATE
import com.samsung.healthcare.kit.R.drawable
import com.samsung.healthcare.kit.app.TaskDataType.Companion.TASK_DATA_TYPE
import com.samsung.healthcare.kit.external.source.HealthPlatformAdapter
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit.DAYS
import kotlin.math.roundToInt

fun DataType.getIcon(): Int =
    when (this) {
        HEART_RATE -> drawable.ic_heart
        SLEEP_SESSION -> drawable.ic_sleep
        TASK_DATA_TYPE -> drawable.ic_task
        else -> TODO()
    }

fun DataType.getPostfix(): String =
    when (this) {
        HEART_RATE -> "\nBPM"
        SLEEP_SESSION -> " hrs\nSleep"
        TASK_DATA_TYPE -> "Tasks\nRemaining"
        else -> TODO()
    }

fun DataType.getDataKey(): String =
    when (this) {
        HEART_RATE -> "bpm"
        else -> TODO()
    }

suspend fun DataType.getLatestStatus(): Any? =
    when (this) {
        is SampleDataType -> getLatestSampleData(this)
        SLEEP_SESSION -> getLatestSleepSession()
        TASK_DATA_TYPE -> null
        else -> TODO()
    }

suspend fun getLatestSleepSession(): Any? {
    val yesterday = midnight().minus(1, DAYS)
    val healthData = HealthPlatformAdapter.getInstance().getHealthData(
        yesterday.toString(),
        Instant.now().toString(),
        SLEEP_SESSION.name
    )

    if (healthData.data.isEmpty()) return null
    val latestSleep = healthData.data.last()
    val startTime = latestSleep["startTime"] as String
    val endTime = latestSleep["endTime"] as String
    val sleepDuration = Duration.between(
        Instant.parse(startTime),
        Instant.parse(endTime)
    ).toMinutes() / 60.0

    return (sleepDuration * 10).roundToInt() / 10.0
}

suspend fun getLatestSampleData(sampleDataType: SampleDataType): Any? {
    val healthData = HealthPlatformAdapter.getInstance().getHealthData(
        midnight().toString(),
        Instant.now().toString(),
        sampleDataType.name
    )

    if (healthData.data.isEmpty()) return null

    return healthData.data.last()[sampleDataType.getDataKey()]
}

private fun midnight() = Instant.now().truncatedTo(DAYS)
