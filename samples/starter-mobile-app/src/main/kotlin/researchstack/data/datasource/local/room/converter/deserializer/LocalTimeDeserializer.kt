package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalTime

internal class LocalTimeDeserializer : JsonDeserializer<LocalTime?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): LocalTime {
        return LocalTime.parse(json.asString)
    }
}
