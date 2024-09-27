package researchstack.wearable.standalone.data.datasource.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import researchstack.wearable.standalone.data.datasource.local.room.converter.StringListConverter
import researchstack.wearable.standalone.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.wearable.standalone.data.datasource.local.room.dao.StudyDao
import researchstack.wearable.standalone.data.datasource.local.room.entity.ShareAgreementEntity
import researchstack.wearable.standalone.data.datasource.local.room.entity.StudyEntity

@Database(
    version = 4,
    exportSchema = false,
    entities = [
        StudyEntity::class,
        ShareAgreementEntity::class,
    ],
)
@TypeConverters(
    value = [
        StringListConverter::class,
    ]
)
abstract class ResearchAppDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao
    abstract fun shareAgreementDao(): ShareAgreementDao

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
                    .addTypeConverter(StringListConverter())
                    .build()
                INSTANCE = instance
                instance
            }
    }
}
