package researchstack.data.datasource.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class MapConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToMap(value: String): Map<String, String> =
        gson.fromJson(value, object : TypeToken<Map<String, String>>() {}.type)

    @TypeConverter
    fun mapToString(value: Map<String, String>?): String =
        if (value == null) "" else gson.toJson(value)
}
