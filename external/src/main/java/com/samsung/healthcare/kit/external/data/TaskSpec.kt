package com.samsung.healthcare.kit.external.data

import com.google.gson.annotations.SerializedName

data class TaskSpec(
    val revisionId: Int,
    @SerializedName("id")
    val taskId: String,
    val title: String,
    val description: String,
    val schedule: String,
    val startTime: String,
    val endTime: String,
    val validTime: Long,
    val items: List<Item>,
)

data class Item(
    val name: String,
    val type: String,
    val contents: Contents,
    val sequence: Int,
)

data class Contents(
    val type: String,
    val title: String,
    val explanation: String? = null,
    @SerializedName("properties")
    val itemProperties: ItemProperties,
    val required: Boolean,
)

open class ItemProperties(
    val tag: String,
)

class ChoiceProperties(
    tag: String,
    val options: List<Option>,
) : ItemProperties(tag)

class ScaleProperties(
    tag: String,
    val low: Int,
    val high: Int,
    val lowLabel: String?,
    val highLabel: String?,
) : ItemProperties(tag)

data class Option(
    val value: String,
)
