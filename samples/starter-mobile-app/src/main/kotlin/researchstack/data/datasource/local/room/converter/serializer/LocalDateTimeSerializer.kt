package researchstack.data.datasource.local.room.converter.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime?> {
    override fun serialize(
        localDateTime: LocalDateTime?,
        srcType: Type?,
        context: JsonSerializationContext?,
    ): JsonElement {
        return JsonPrimitive(localDateTime.toString())
    }
}
