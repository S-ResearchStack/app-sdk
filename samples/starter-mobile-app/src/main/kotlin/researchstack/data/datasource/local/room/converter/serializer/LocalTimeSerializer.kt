package researchstack.data.datasource.local.room.converter.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalTime

class LocalTimeSerializer : JsonSerializer<LocalTime?> {
    override fun serialize(
        localTime: LocalTime?,
        srcType: Type?,
        context: JsonSerializationContext?,
    ): JsonElement {
        return JsonPrimitive(localTime.toString())
    }
}
