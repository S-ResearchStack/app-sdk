package researchstack.data.datasource.local.room.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import researchstack.data.datasource.local.room.converter.deserializer.AnswerDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.LocalDateDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.LocalDateTimeDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.LocalTimeDeserializer
import researchstack.data.datasource.local.room.converter.deserializer.QuestionDeserializer
import researchstack.data.datasource.local.room.converter.serializer.LocalDateSerializer
import researchstack.data.datasource.local.room.converter.serializer.LocalDateTimeSerializer
import researchstack.data.datasource.local.room.converter.serializer.LocalTimeSerializer
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.domain.model.task.Section
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ProvidedTypeConverter
class EligibilityConverter {
    private val serializeGson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
        .create()

    private val deserializeGson = GsonBuilder()
        .registerTypeAdapter(Answer::class.java, AnswerDeserializer())
        .registerTypeAdapter(Question::class.java, QuestionDeserializer())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
        .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .create()

    @TypeConverter
    fun convertFromAnswersToJson(answers: List<Answer>): String =
        serializeGson.toJson(answers)

    @TypeConverter
    fun convertFromSectionsToJson(sections: List<Section>): String =
        serializeGson.toJson(sections)

    @TypeConverter
    fun convertFromHealthDataTypesToJson(answerEntities: List<SHealthDataType>): String =
        serializeGson.toJson(answerEntities)

    @TypeConverter
    fun convertFromTrackerTypesToJson(answerEntities: List<TrackerDataType>): String =
        serializeGson.toJson(answerEntities)

    @TypeConverter
    fun convertFromPrivDataTypesToJson(answerEntities: List<PrivDataType>): String =
        serializeGson.toJson(answerEntities)

    @TypeConverter
    fun convertFromDeviceStatDataTypesToJson(answerEntities: List<DeviceStatDataType>): String =
        serializeGson.toJson(answerEntities)

    @TypeConverter
    fun convertFromSurveyResultsToJson(surveyResults: SurveyResult?): String? =
        surveyResults?.let { serializeGson.toJson(surveyResults) }

    @TypeConverter
    fun convertFromJsonToAnswers(json: String): List<Answer> {
        val listType = object : TypeToken<ArrayList<Answer>>() {}.type
        return deserializeGson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToSections(json: String): List<Section> {
        val listType = object : TypeToken<ArrayList<Section>>() {}.type
        return deserializeGson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToHealthDataTypes(json: String): List<SHealthDataType> {
        val listType = object : TypeToken<ArrayList<SHealthDataType>>() {}.type
        return deserializeGson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToTrackerDataTypes(json: String): List<TrackerDataType> {
        val listType = object : TypeToken<ArrayList<TrackerDataType>>() {}.type
        return deserializeGson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToPrivDataTypes(json: String): List<PrivDataType> {
        val listType = object : TypeToken<ArrayList<PrivDataType>>() {}.type
        return deserializeGson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToDeviceStatDataTypes(json: String): List<DeviceStatDataType> {
        val listType = object : TypeToken<ArrayList<DeviceStatDataType>>() {}.type
        return deserializeGson.fromJson(json, listType)
    }

    @TypeConverter
    fun convertFromJsonToSurveyResult(json: String?): SurveyResult? =
        json?.let { deserializeGson.fromJson(json, SurveyResult::class.java) }
}
