package healthstack.app.task.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import healthstack.app.task.utils.ItemToModelUtil
import healthstack.backend.integration.task.ChoiceProperties
import healthstack.backend.integration.task.DateTimeProperties
import healthstack.backend.integration.task.Item
import healthstack.backend.integration.task.RankingProperties
import healthstack.backend.integration.task.ScaleProperties
import healthstack.backend.integration.task.TextProperties
import healthstack.kit.task.activity.predefined.PredefinedTaskUtil
import healthstack.kit.task.survey.SurveyTask
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.task.survey.question.model.SkipLogic
import java.time.LocalDateTime

internal const val CHOICE = "CHOICE"
internal const val SCALE = "SCALE"
internal const val TEXT = "TEXT"
internal const val DATETIME = "DATETIME"
internal const val RANK = "RANK"

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
                        SCALE -> toScaleQuestionModel(it)
                        TEXT -> toTextQuestionModel(it)
                        RANK -> toRankQuestionModel(it)
                        DATETIME -> toDateTimeQuestionModel(it)
                        else -> throw NotImplementedError("not supported content type")
                    }
                )
            }
        }.build()

        else -> throw IllegalArgumentException("not supported task type")
    }

    private fun toRankQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == RANK)
        require(item.contents.itemProperties is RankingProperties)

        return ItemToModelUtil.toRank(item) as QuestionModel<Any>
    }

    private fun toTextQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == TEXT)
        require(item.contents.itemProperties is TextProperties)

        return ItemToModelUtil.toText(item) as QuestionModel<Any>
    }

    private fun toScaleQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == SCALE)
        require(item.contents.itemProperties is ScaleProperties)

        return ItemToModelUtil.toScale(item)
    }

    private fun toDateTimeQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == DATETIME)
        require(item.contents.itemProperties is DateTimeProperties)

        return ItemToModelUtil.toDateTime(item) as QuestionModel<Any>
    }

    private fun toChoiceQuestionModel(item: Item): QuestionModel<Any> {
        require(item.contents.type == CHOICE)
        require(item.contents.itemProperties is ChoiceProperties)

        return when (item.contents.itemProperties!!.tag.uppercase()) {
            "CHECKBOX" -> ItemToModelUtil.toMultiChoice(item) as QuestionModel<Any>
            "IMAGE", "MULTIIMAGE" -> ItemToModelUtil.toImageChoice(item) as QuestionModel<Any>
            else -> ItemToModelUtil.toChoice(item)
        }
    }
}

fun healthstack.backend.integration.task.SkipLogic.translate(): SkipLogic =
    SkipLogic(condition, goToItemSequence)
