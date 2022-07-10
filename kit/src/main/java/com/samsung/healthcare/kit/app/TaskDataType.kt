package com.samsung.healthcare.kit.app

import com.google.android.libraries.healthdata.data.DataType
import com.google.android.libraries.healthdata.data.Field

class TaskDataType private constructor() : DataType {
    companion object {
        val TASK_DATA_TYPE = TaskDataType()
    }

    override fun getFieldFromName(p0: String): Field =
        throw NoSuchFieldError()

    override fun getName(): String = "TaskDataType"

    override fun getAllFields(): Set<Field> = emptySet()

    override fun getOptionalFields(): Set<Field> = emptySet()

    override fun getRequiredFields(): Set<Field> = emptySet()
}
