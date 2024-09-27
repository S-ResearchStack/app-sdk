package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDateTime

internal class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): LocalDateTime {
        return LocalDateTime.parse(json.asString)
    }
}
