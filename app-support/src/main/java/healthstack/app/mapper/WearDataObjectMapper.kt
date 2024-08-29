package healthstack.app.mapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.IOException

object WearDataObjectMapper {
    private val objectMapper = jacksonObjectMapper().apply {
        class ListDeserializer : JsonDeserializer<List<*>>(), ContextualDeserializer {
            private val objectMapper = ObjectMapper()
            private val types: MutableMap<String, JavaType> = mutableMapOf()

            override fun createContextual(
                ctxt: DeserializationContext,
                property: BeanProperty
            ): JsonDeserializer<List<*>> {
                types[property.name] = property.type
                return this
            }

            @Throws(IOException::class)
            override fun deserialize(
                jsonParser: JsonParser,
                deserializationContext: DeserializationContext
            ): List<*> {
                val node: JsonNode = jsonParser.getCodec().readTree(jsonParser)
                val jsonStr = node.toString().drop(1).dropLast(1).replace("\\\"", "\"")
                return objectMapper.readValue(jsonStr, types[jsonParser.parsingContext.currentName])
            }
        }
        val listModule = SimpleModule().addDeserializer(List::class.java, ListDeserializer())
        registerModule(listModule)
        registerModule(JavaTimeModule())
    }

    fun getInstance() = objectMapper
}
