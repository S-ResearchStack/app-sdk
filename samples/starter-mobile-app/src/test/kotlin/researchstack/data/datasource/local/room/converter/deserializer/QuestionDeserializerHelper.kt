package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializer
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.domain.model.task.question.RankQuestion
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag

class QuestionDeserializerHelper : JsonDeserializerHelper<Question>() {

    override var elements: Array<Question> = arrayOf(
        ChoiceQuestion(
            "id",
            "title",
            "explanation",
            QuestionTag.CHECKBOX,
            true,
            listOf(Option("value", "label"))
        ),
        DateTimeQuestion(
            "id",
            "title",
            "explanation",
            QuestionTag.DATETIME,
            true,
            true,
            true,
            true,
        ),
        RankQuestion(
            "id",
            "title",
            "explanation",
            QuestionTag.RANK,
            true,
            listOf(Option("value", "label"))
        ),
        ScaleQuestion(
            "id",
            "title",
            "explanation",
            QuestionTag.SLIDER,
            true,
            0,
            100,
            "lowLabel",
            "highLabel",
        ),
        TextQuestion(
            "id",
            "title",
            "explanation",
            QuestionTag.TEXT,
            true,
        ),
    )
    override var jsonDeserializer: JsonDeserializer<Question> = QuestionDeserializer()
}
