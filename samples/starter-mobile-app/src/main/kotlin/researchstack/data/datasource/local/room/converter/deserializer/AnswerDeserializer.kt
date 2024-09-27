package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.eligibilitytest.answer.ChoiceAnswer
import researchstack.domain.model.eligibilitytest.answer.DateTimeAnswer
import researchstack.domain.model.eligibilitytest.answer.RankingAnswer
import researchstack.domain.model.eligibilitytest.answer.ScaleAnswer
import researchstack.domain.model.eligibilitytest.answer.TextAnswer
import researchstack.domain.model.task.question.common.QuestionType
import java.lang.reflect.Type

class AnswerDeserializer : JsonDeserializer<Answer> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Answer? {
        val questionType =
            if (json.asJsonObject.has("type")) json.asJsonObject["type"].asString
            else return null

        return when (QuestionType.valueOf(questionType)) {
            QuestionType.CHOICE -> context.deserialize(json, ChoiceAnswer::class.java)
            QuestionType.DATETIME -> context.deserialize(json, DateTimeAnswer::class.java)
            QuestionType.RANK -> context.deserialize(json, RankingAnswer::class.java)
            QuestionType.SCALE -> context.deserialize(json, ScaleAnswer::class.java)
            QuestionType.TEXT -> context.deserialize(json, TextAnswer::class.java)
        }
    }
}
