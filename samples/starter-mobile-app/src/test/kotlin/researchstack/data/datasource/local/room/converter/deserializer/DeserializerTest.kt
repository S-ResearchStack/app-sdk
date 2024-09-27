package researchstack.data.datasource.local.room.converter.deserializer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.taskresult.TaskResult

class DeserializerTest {

    private fun Class<*>.getHelper(): JsonDeserializerHelper<*> = when (this) {
        Answer::class.java -> AnswerDeserializerHelper()
        Task::class.java -> TaskDeserializerHelper()
        TaskResult::class.java -> TaskResultDeserializerHelper()
        Question::class.java -> QuestionDeserializerHelper()
        else -> throw Exception()
    }

    @ParameterizedTest
    @ValueSource(
        classes = [Answer::class, Task::class, TaskResult::class, Question::class]
    )
    @Tag(POSITIVE_TEST)
    fun `deserialize should return correct object`(clazz: Class<*>) {
        val helper = clazz.getHelper()
        val res = helper.`deserialize should return correct object`(clazz)
        for (i in helper.elements.indices) {
            val test = res[i].hashCode() == helper.elements[i].hashCode()
            val _test =
                helper.serializeGson.toJson(helper.elements[i]) == helper.serializeGson.toJson(res[i])
            Assertions.assertTrue(test || _test)
        }
    }

    @ParameterizedTest
    @ValueSource(
        classes = [Answer::class, Task::class, TaskResult::class, Question::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `deserialize should return null`(clazz: Class<*>) {
        Assertions.assertNull(clazz.getHelper().`deserialize should return null`())
    }
}
