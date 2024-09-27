package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate

internal class LocalDateDeserializer : JsonDeserializer<LocalDate?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): LocalDate {
        return LocalDate.parse(json.getAsJsonPrimitive().getAsString())
    }
}
