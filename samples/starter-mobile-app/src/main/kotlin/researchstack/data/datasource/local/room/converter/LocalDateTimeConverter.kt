package researchstack.data.datasource.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime

@ProvidedTypeConverter
class LocalDateTimeConverter {
    @TypeConverter
    fun timeToString(value: LocalDateTime?): String? = value?.toString()

    @TypeConverter
    fun stringToTime(value: String?): LocalDateTime? =
        runCatching {
            LocalDateTime.parse(value)
        }.getOrNull()
}
