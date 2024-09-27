package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.mockk.mockk
import researchstack.data.datasource.local.room.converter.serializer.LocalDateSerializer
import researchstack.data.datasource.local.room.converter.serializer.LocalDateTimeSerializer
import researchstack.data.datasource.local.room.converter.serializer.LocalTimeSerializer
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

abstract class JsonDeserializerHelper<T> {
    val serializeGson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .create()

    private val deserializeGson: Gson = GsonBuilder()
        .registerTypeAdapter(Task::class.java, TaskDeserializer())
        .registerTypeAdapter(TaskResult::class.java, TaskResultDeserializer())
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .registerTypeAdapter(Answer::class.java, AnswerDeserializer())
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
        .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        .create()

    abstract var elements: Array<T>
    protected abstract var jsonDeserializer: JsonDeserializer<T>
    private val jsonDeserializationContext = mockk<JsonDeserializationContext>()

    fun `deserialize should return null`(): Any? {
        val jsonElement = JsonParser.parseString("{}")
        val type = object : TypeToken<String>() {}.type
        return jsonDeserializer.deserialize(
            jsonElement,
            type,
            jsonDeserializationContext
        )
    }

    fun `deserialize should return correct object`(type: Class<*>): List<T> {
        val res = arrayListOf<T>()
        elements.forEach { element ->
            val str = serializeGson.toJson(element)
            val jsonElement = JsonParser.parseString(str)
            res.add(deserializeGson.fromJson(jsonElement, type) as T)
        }
        return res
    }
}
