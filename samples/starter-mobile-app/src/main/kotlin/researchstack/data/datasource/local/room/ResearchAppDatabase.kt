package researchstack.data.datasource.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import researchstack.data.datasource.local.room.converter.EligibilityConverter
import researchstack.data.datasource.local.room.converter.LocalDateTimeConverter
import researchstack.data.datasource.local.room.converter.MapConverter
import researchstack.data.datasource.local.room.converter.StringListConverter
import researchstack.data.datasource.local.room.converter.TaskConverter
import researchstack.data.datasource.local.room.dao.AccelerometerDao
import researchstack.data.datasource.local.room.dao.EventDao
import researchstack.data.datasource.local.room.dao.FileUploadRequestDao
import researchstack.data.datasource.local.room.dao.LightDao
import researchstack.data.datasource.local.room.dao.LogDao
import researchstack.data.datasource.local.room.dao.ParticipationRequirementDao
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.SpeedDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.data.datasource.local.room.entity.AppLogEntity
import researchstack.data.datasource.local.room.entity.EventEntity
import researchstack.data.datasource.local.room.entity.FileUploadRequestEntity
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity
import researchstack.data.datasource.local.room.entity.ShareAgreementEntity
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.data.datasource.local.room.entity.StudyEntity
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.domain.model.sensor.Accelerometer
import researchstack.domain.model.sensor.Light

@Database(
    version = 4,
    exportSchema = false,
    entities = [
        StudyEntity::class,
        ParticipationRequirementEntity::class,
        TaskEntity::class,
        ShareAgreementEntity::class,
        Light::class,
        Accelerometer::class,
        AppLogEntity::class,
        Speed::class,
        FileUploadRequestEntity::class,
        EventEntity::class,
    ],
)
@TypeConverters(
    value = [
        EligibilityConverter::class,
        TaskConverter::class,
        LocalDateTimeConverter::class,
        StringListConverter::class,
        MapConverter::class
    ]
)
abstract class ResearchAppDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao
    abstract fun participationRequirementDao(): ParticipationRequirementDao
    abstract fun taskDao(): TaskDao
    abstract fun shareAgreementDao(): ShareAgreementDao
    abstract fun lightDao(): LightDao
    abstract fun speedDao(): SpeedDao
    abstract fun accelerometerDao(): AccelerometerDao
    abstract fun logDao(): LogDao
    abstract fun fileUploadRequestDao(): FileUploadRequestDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: ResearchAppDatabase? = null

        fun getDatabase(
            context: Context,
        ): ResearchAppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResearchAppDatabase::class.java,
                    "research_app_db"
                )
                    .fallbackToDestructiveMigration()
                    .addTypeConverter(EligibilityConverter())
                    .addTypeConverter(LocalDateTimeConverter())
                    .addTypeConverter(TaskConverter())
                    .addTypeConverter(StringListConverter())
                    .addTypeConverter(MapConverter())
                    .build()
                INSTANCE = instance
                instance
            }
    }
}
