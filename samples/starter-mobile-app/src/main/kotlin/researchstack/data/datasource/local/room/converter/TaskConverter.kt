package researchstack.data.datasource.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import researchstack.data.datasource.local.room.converter.deserializer.LocalDateTimeDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.QuestionDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.TaskDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.TaskResultDeserializer
import researchstack.data.datasource.local.room.converter.serializer.LocalDateTimeSerializer
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDateTime

@ProvidedTypeConverter
class TaskConverter {
    private val serializeGson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .create()

    private val deserializeGson: Gson = GsonBuilder()
        .registerTypeAdapter(Task::class.java, TaskDeserializer())
        .registerTypeAdapter(TaskResult::class.java, TaskResultDeserializer())
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .create()

    @TypeConverter
    fun taskToJson(value: Task): String = serializeGson.toJson(value)

    @TypeConverter
    fun taskResultToJson(taskResult: TaskResult?): String? =
        taskResult?.let { serializeGson.toJson(taskResult) }

    @TypeConverter
    fun jsonToTask(value: String): Task = deserializeGson.fromJson(value, Task::class.java)

    @TypeConverter
    fun jsonToTaskResult(value: String?): TaskResult? =
        value?.let { deserializeGson.fromJson(value, TaskResult::class.java) }
}
