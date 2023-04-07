package healthstack.backend.integration.task

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class PropertyDeserializer : JsonDeserializer<ItemProperties> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): ItemProperties? {
        val jsonObject: JsonObject = json?.asJsonObject ?: throw NullPointerException()

        if (!jsonObject.has("tag"))
        // TODO refactor branching logic
            return null

        return when (jsonObject["tag"].asString.uppercase()) {
            "SLIDER" -> context?.deserialize(json, ScaleProperties::class.java)
            "RADIO", "CHECKBOX" -> context?.deserialize(
                json,
                ChoiceProperties::class.java
            )
            else -> throw IllegalArgumentException("Not supported tag.")
        }
    }
}
