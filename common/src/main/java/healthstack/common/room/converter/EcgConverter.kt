package healthstack.common.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import healthstack.common.model.Ecg
import healthstack.common.model.PpgGreen

@ProvidedTypeConverter
class EcgConverter {
    private val gson = Gson()

    @TypeConverter
    fun convertEcgToJson(ecgs: List<Ecg>): String = gson.toJson(ecgs)

    @TypeConverter
    fun convertPpgGreenToJson(ppgGreens: List<PpgGreen>): String = gson.toJson(ppgGreens)

    @TypeConverter
    fun convertJsonToEcg(json: String): List<Ecg> {
        val listType = object : TypeToken<ArrayList<Ecg>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertJsonToPpgGreen(json: String): List<PpgGreen> {
        val listType = object : TypeToken<ArrayList<PpgGreen>>() {}.type
        return gson.fromJson(json, listType)
    }
}
