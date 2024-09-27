package researchstack.data.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import researchstack.domain.model.priv.Ecg
import researchstack.domain.model.priv.PpgGreen

@ProvidedTypeConverter
class ECGConverter {
    private val gson = Gson()

    @TypeConverter
    fun convertFromEcgsToJson(ecgs: List<Ecg>): String = gson.toJson(ecgs)

    @TypeConverter
    fun convertFromPpgGreensToJson(ppgGreens: List<PpgGreen>): String = gson.toJson(ppgGreens)

    @TypeConverter
    fun convertFromJsonToEcgs(json: String): List<Ecg> {
        val listType = object : TypeToken<ArrayList<Ecg>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToPpgGreens(json: String): List<PpgGreen> {
        val listType = object : TypeToken<ArrayList<PpgGreen>>() {}.type
        return gson.fromJson(json, listType)
    }
}
