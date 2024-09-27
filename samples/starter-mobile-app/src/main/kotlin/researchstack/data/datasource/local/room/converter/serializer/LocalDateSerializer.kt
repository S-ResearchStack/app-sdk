package researchstack.data.datasource.local.room.converter.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate

class LocalDateSerializer : JsonSerializer<LocalDate?> {
    override fun serialize(
        localDateTime: LocalDate?,
        srcType: Type?,
        context: JsonSerializationContext?,
    ): JsonElement {
        return JsonPrimitive(localDateTime.toString())
    }
}
