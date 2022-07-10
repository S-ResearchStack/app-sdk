package com.samsung.healthcare.kit.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samsung.healthcare.kit.external.data.ChoiceProperties
import com.samsung.healthcare.kit.external.data.Item
import com.samsung.healthcare.kit.external.data.ScaleProperties
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel.ViewType
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel.ViewType.Slider
import com.samsung.healthcare.kit.model.question.MultiChoiceQuestionModel
import com.samsung.healthcare.kit.model.question.QuestionModel
import com.samsung.healthcare.kit.task.SurveyTask
import java.time.LocalDateTime

@Entity
data class Task(
    @PrimaryKey
    val id: Int? = null,
    val revisionId: Int,
    val taskId: String,
    val properties: Properties,
    val result: List<Result>? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val scheduledAt: LocalDateTime? = null,
    val validUntil: LocalDateTime? = null,
    val submittedAt: LocalDateTime? = null,
    val startedAt: LocalDateTime? = null,
) {
    data class Properties(
        val title: String,
        val description: String,
        val items: List<Item>,
    )

    data class Result(
        val questionId: String,
        val response: String,
    )

    fun toViewTask(): SurveyTask = SurveyTask.Builder(
        id!!.toString(), // TODO
        revisionId,
        taskId,
        properties.title,
        properties.description,
        {},
        isCompleted = result != null
    ).apply {
        properties.items.map {
            this.addQuestion(
                when (it.contents.type) {
                    "CHOICE" -> toChoiceQuestionModel(it)
                    "SCALE" -> toSliderQuestionModel(it)
                    else -> throw Error("item view type is incorrect")
                }
            )
        }
    }.build()

    private fun toSliderQuestionModel(item: Item): QuestionModel<Any> =
        ChoiceQuestionModel(
            item.name,
            item.contents.title,
            item.contents.explanation,
            null,
            null,
            listOf(
                (item.contents.itemProperties as ScaleProperties).low,
                (item.contents.itemProperties as ScaleProperties).high
            ),
            Slider
        )

    private fun toChoiceQuestionModel(item: Item): QuestionModel<Any> =
        if (item.contents.itemProperties.tag.uppercase() == "CHECKBOX") toMultiChoiceQuestionModel(item) as QuestionModel<Any>
        else ChoiceQuestionModel(
            item.name,
            item.contents.title,
            item.contents.explanation,
            null,
            null,
            (item.contents.itemProperties as ChoiceProperties).options.map { option -> option.value },
            ViewType.values()
                .first { type -> type.name.equals(item.contents.itemProperties.tag, ignoreCase = true) }
        )

    private fun toMultiChoiceQuestionModel(item: Item): MultiChoiceQuestionModel =
        MultiChoiceQuestionModel(
            item.name,
            item.contents.title,
            item.contents.explanation,
            null,
            null,
            (item.contents.itemProperties as ChoiceProperties).options.map { option -> option.value },
        )
}
