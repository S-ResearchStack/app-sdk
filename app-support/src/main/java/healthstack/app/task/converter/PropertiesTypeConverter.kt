package healthstack.app.task.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import healthstack.app.task.entity.Task.Properties
import healthstack.backend.integration.task.ItemProperties
import healthstack.backend.integration.task.PropertyDeserializer

@ProvidedTypeConverter
class PropertiesTypeConverter {

    private val gson: Gson = Gson()

    private val gsonWithConverter: Gson = GsonBuilder()
        .registerTypeAdapter(ItemProperties::class.java, PropertyDeserializer())
        .create()

    @TypeConverter
    fun objectToJson(value: Properties): String = gson.toJson(value)

    @TypeConverter
    fun jsonToObject(value: String): Properties = gsonWithConverter.fromJson(value, Properties::class.java)
}
