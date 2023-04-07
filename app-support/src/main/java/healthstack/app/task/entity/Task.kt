package healthstack.app.task.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import healthstack.backend.integration.task.ChoiceProperties
import healthstack.backend.integration.task.Item
import healthstack.backend.integration.task.ScaleProperties
import healthstack.kit.task.activity.predefined.PredefinedTaskUtil
import healthstack.kit.task.survey.SurveyTask
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Slider
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.task.survey.question.model.SkipLogic
import java.time.LocalDateTime

internal const val CHOICE = "CHOICE"
internal const val SCALE = "SCALE"

@Entity(indices = [Index(value = ["revisionId", "taskId", "scheduledAt"], unique = true)])
data class Task(
    @PrimaryKey
    val id: Int? = null,
    val revisionId: Int,
    val taskId: String,
    val type: String,
    val properties: Properties,
    val result: List<Result>? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val scheduledAt: LocalDateTime,
    val validUntil: LocalDateTime,
    val submittedAt: LocalDateTime? = null,
    val startedAt: LocalDateTime? = null,
) {
    data class Properties(
        val title: String,
        val description: String?,
        val items: List<Item>,
    )

    data class Result(
        val questionId: String,
        val response: String,
    )

    fun toViewTask() = when (this.type) {
        "ACTIVITY" -> PredefinedTaskUtil.generatePredefinedTask(
            id!!.toString(),
            taskId,
            properties.title,
            properties.description ?: "",
            isCompleted = result != null,
            isActive = LocalDateTime.now().let {
                scheduledAt <= it && it <= validUntil
            },
            properties.items[0].contents.completionTitle ?: "",
            listOf(properties.items[0].contents.completionDescription ?: ""),
            activityType = this.properties.items[0].contents.type
        )
        "SURVEY" -> SurveyTask.Builder(
            id!!.toString(), // TODO
            revisionId,
            taskId,
            properties.title,
            properties.description ?: "",
            {},
            isCompleted = result != null,
            isActive = LocalDateTime.now().let {
                scheduledAt <= it && it <= validUntil
            }
        ).apply {
            properties.items.map {
                if (it.type == "SECTION") this.addSection()
                else this.addQuestion(
                    when (it.contents.type) {
                        CHOICE -> toChoiceQuestionModel(it)
                        SCALE -> toSliderQuestionModel(it)
                        else -> throw NotImplementedError("not supported content type")
                    }
                )
            }
        }.build()
        else -> throw IllegalArgumentException("not supported task type")
    }

    private fun toSliderQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == SCALE)
        require(item.contents.itemProperties is ScaleProperties)

        return ChoiceQuestionModel(
            item.name,
            item.contents.title!!,
            item.contents.explanation,
            null,
            null,
            (item.contents.itemProperties as ScaleProperties).skipLogic?.map { it.translate() } ?: emptyList(),
            listOf(
                (item.contents.itemProperties as ScaleProperties).low,
                (item.contents.itemProperties as ScaleProperties).high
            ),
            Slider
        )
    }

    private fun toChoiceQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == CHOICE)
        require(item.contents.itemProperties is ChoiceProperties)

        return if (item.contents.itemProperties!!.tag.uppercase() == "CHECKBOX")
            toMultiChoiceQuestionModel(item) as QuestionModel<Any>
        else ChoiceQuestionModel(
            item.name,
            item.contents.title!!,
            item.contents.explanation,
            null,
            null,
            (item.contents.itemProperties as ChoiceProperties).skipLogic?.map { it.translate() } ?: emptyList(),
            (item.contents.itemProperties as ChoiceProperties).options.map { option -> option.value },
            ViewType.values()
                .first { type -> type.name.equals(item.contents.itemProperties!!.tag, ignoreCase = true) }
        )
    }

    private fun toMultiChoiceQuestionModel(item: Item): MultiChoiceQuestionModel {
        return MultiChoiceQuestionModel(
            item.name,
            item.contents.title!!,
            item.contents.explanation,
            null,
            null,
            (item.contents.itemProperties as ChoiceProperties).skipLogic?.map { it.translate() } ?: emptyList(),
            (item.contents.itemProperties as ChoiceProperties).options.map { option -> option.value },
        )
    }

    private fun healthstack.backend.integration.task.SkipLogic.translate(): SkipLogic {
        return SkipLogic(condition, goToItemSequence)
    }
}
