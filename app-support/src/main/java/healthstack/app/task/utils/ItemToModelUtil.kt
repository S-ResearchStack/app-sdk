package healthstack.app.task.utils

import healthstack.app.task.entity.translate
import healthstack.backend.integration.task.ChoiceProperties
import healthstack.backend.integration.task.DateTimeProperties
import healthstack.backend.integration.task.Item
import healthstack.backend.integration.task.RankingProperties
import healthstack.backend.integration.task.ScaleProperties
import healthstack.backend.integration.task.TextProperties
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Slider
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel
import healthstack.kit.task.survey.question.model.ImageChoiceQuestionModel
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel
import healthstack.kit.task.survey.question.model.RankingQuestionModel
import healthstack.kit.task.survey.question.model.TextInputQuestionModel

object ItemToModelUtil {
    fun toMultiChoice(item: Item): MultiChoiceQuestionModel {
        val choiceProperties: ChoiceProperties = item.contents.itemProperties as ChoiceProperties
        return MultiChoiceQuestionModel(
            id = item.name,
            query = item.contents.title!!,
            explanation = item.contents.explanation,
            drawableId = null,
            skipLogics = choiceProperties.skipLogic?.map { it.translate() }
                ?: emptyList(),
            candidates = choiceProperties.options.map { option -> option.value },
            tag = choiceProperties.tag
        )
    }

    fun toImageChoice(item: Item): ImageChoiceQuestionModel {
        val imageProperties: ChoiceProperties = item.contents.itemProperties as ChoiceProperties
        return ImageChoiceQuestionModel(
            id = item.name,
            query = item.contents.title!!,
            explanation = item.contents.explanation,
            drawableId = null,
            skipLogics = imageProperties.skipLogic?.map { it.translate() }
                ?: emptyList(),
            candidates = imageProperties.options.map { option -> option.value },
            labels = imageProperties.options.mapNotNull { option -> option.label },
            tag = imageProperties.tag
        )
    }

    fun toDateTime(item: Item): DateTimeQuestionModel {
        val dateTimeProperties: DateTimeProperties = item.contents.itemProperties as DateTimeProperties

        return DateTimeQuestionModel(
            id = item.name,
            query = item.contents.title!!,
            explanation = item.contents.explanation,
            drawableId = null,
            skipLogics = dateTimeProperties.skipLogic?.map { it.translate() }
                ?: emptyList(),
            isTime = dateTimeProperties.isTime,
            isDate = dateTimeProperties.isDate,
            isRange = dateTimeProperties.isRange
        )
    }

    fun toChoice(item: Item): ChoiceQuestionModel<Any> =
        ChoiceQuestionModel(
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

    fun toScale(item: Item): ChoiceQuestionModel<Any> {
        val scaleProperties = item.contents.itemProperties as ScaleProperties

        return ChoiceQuestionModel(
            item.name,
            item.contents.title!!,
            item.contents.explanation,
            null,
            null,
            scaleProperties.skipLogic?.map { it.translate() } ?: emptyList(),
            listOf(
                scaleProperties.low,
                scaleProperties.high
            ),
            Slider
        )
    }

    fun toText(item: Item): TextInputQuestionModel =
        TextInputQuestionModel(
            id = item.name,
            query = item.contents.title!!,
            explanation = item.contents.explanation,
            drawableId = null,
            answer = null,
            skipLogics = (item.contents.itemProperties as TextProperties).skipLogic?.map { it.translate() }
                ?: emptyList(),
        )

    fun toRank(item: Item): RankingQuestionModel {
        val rankingProperties = item.contents.itemProperties as RankingProperties

        return RankingQuestionModel(
            id = item.name,
            query = item.contents.title!!,
            explanation = item.contents.explanation,
            drawableId = null,
            answer = null,
            skipLogics = rankingProperties.skipLogic?.map { it.translate() } ?: emptyList(),
            candidates = rankingProperties.options.map { option -> option.value }
        )
    }
}
