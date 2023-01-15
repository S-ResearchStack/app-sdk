package healthstack.app.task.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import healthstack.app.task.entity.Task.Result
import java.lang.reflect.Type

@ProvidedTypeConverter
class ResultConverter(private val gson: Gson) {
    @TypeConverter
    fun objectToJson(value: List<Result>?): String? =
        if (null == value)
            null
        else
            gson.toJson(value)

    private val resultsType: Type = object : TypeToken<List<Result>>() {}.type

    @TypeConverter
    fun jsonToObject(value: String?): List<Result>? =
        if (null == value)
            null
        else
            gson.fromJson(value, resultsType)
}
