package researchstack.wearable.standalone.data.datasource.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class StringListConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringListToJson(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> = gson.fromJson(value, Array<String>::class.java).toList()
}
