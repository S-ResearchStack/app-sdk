package researchstack.data.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class HeartRateConverter {
    private val gson = Gson()

    @TypeConverter
    fun convertFromListToJson(ibiList: List<Int>): String = gson.toJson(ibiList)

    @TypeConverter
    fun convertFromJsonToList(json: String): List<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, listType)
    }
}
