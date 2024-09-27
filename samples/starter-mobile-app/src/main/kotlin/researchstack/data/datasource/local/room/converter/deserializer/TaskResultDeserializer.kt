package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import researchstack.domain.model.task.TaskType
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.domain.model.task.taskresult.TaskResult
import java.lang.reflect.Type

class TaskResultDeserializer : JsonDeserializer<TaskResult> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): TaskResult? {
        val type =
            if (json.asJsonObject.has("type")) json.asJsonObject["type"].asString
            else return null

        return when (TaskType.valueOf(type)) {
            TaskType.ACTIVITY -> context.deserialize(
                json,
                ActivityResult::class.java
            )

            TaskType.SURVEY -> context.deserialize(
                json,
                SurveyResult::class.java
            )
        }
    }
}
