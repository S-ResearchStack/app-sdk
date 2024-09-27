package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializer
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.eligibilitytest.answer.ChoiceAnswer
import researchstack.domain.model.eligibilitytest.answer.DateTimeAnswer
import researchstack.domain.model.eligibilitytest.answer.RankingAnswer
import researchstack.domain.model.eligibilitytest.answer.ScaleAnswer
import researchstack.domain.model.eligibilitytest.answer.TextAnswer
import researchstack.domain.model.task.question.common.Option
import java.time.LocalDate
import java.time.LocalTime

class AnswerDeserializerHelper : JsonDeserializerHelper<Answer>() {
    override var elements: Array<Answer> = arrayOf(
        ChoiceAnswer("questionId", listOf(Option("value", "label"))),
        DateTimeAnswer(
            "questionId",
            LocalDate.now(),
            LocalDate.now(),
            LocalTime.now(),
            LocalTime.now()
        ),
        RankingAnswer("questionId", listOf("abc")),
        ScaleAnswer("questionId", 0, 100),
        TextAnswer("questionId", listOf("abc"))
    )

    override var jsonDeserializer = AnswerDeserializer() as JsonDeserializer<Answer>
}
