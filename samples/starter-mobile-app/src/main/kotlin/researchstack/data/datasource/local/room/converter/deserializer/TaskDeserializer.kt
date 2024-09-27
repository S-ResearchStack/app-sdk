package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.SurveyTask
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.TaskType
import java.lang.reflect.Type

class TaskDeserializer : JsonDeserializer<Task> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Task? {
        val type =
            if (json.asJsonObject.has("type")) json.asJsonObject["type"].asString
            else return null

        return when (TaskType.valueOf(type)) {
            TaskType.ACTIVITY -> context.deserialize(
                json,
                ActivityTask::class.java
            )

            TaskType.SURVEY -> context.deserialize(
                json,
                SurveyTask::class.java
            )
        }
    }
}
