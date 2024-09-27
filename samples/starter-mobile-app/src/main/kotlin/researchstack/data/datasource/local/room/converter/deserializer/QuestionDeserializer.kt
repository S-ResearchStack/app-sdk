package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.domain.model.task.question.RankQuestion
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionType
import researchstack.domain.model.task.question.common.QuestionType.CHOICE
import researchstack.domain.model.task.question.common.QuestionType.DATETIME
import researchstack.domain.model.task.question.common.QuestionType.RANK
import researchstack.domain.model.task.question.common.QuestionType.SCALE
import researchstack.domain.model.task.question.common.QuestionType.TEXT
import java.lang.reflect.Type

class QuestionDeserializer : JsonDeserializer<Question> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Question? {
        val type =
            if (json.asJsonObject.has("type")) json.asJsonObject["type"].asString
            else return null

        return when (QuestionType.valueOf(type)) {
            CHOICE -> context.deserialize(json, ChoiceQuestion::class.java)
            SCALE -> context.deserialize(json, ScaleQuestion::class.java)
            DATETIME -> context.deserialize(json, DateTimeQuestion::class.java)
            TEXT -> context.deserialize(json, TextQuestion::class.java)
            RANK -> context.deserialize(json, RankQuestion::class.java)
        }
    }
}
