package healthstack.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import healthstack.common.room.converter.EcgConverter
import healthstack.common.room.dao.EcgDao
import healthstack.common.model.EcgSet

@Database(
    version = 1,
    exportSchema = false,

    entities = [
        EcgSet::class,
    ],
)
@TypeConverters(
    value = [
        EcgConverter::class,
    ]
)
abstract class WearableAppDatabase : RoomDatabase() {
    abstract fun ecgDao(): EcgDao

    companion object {
        @Volatile
        private var INSTANCE: WearableAppDatabase? = null

        fun initialize(
            context: Context,
        ): WearableAppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WearableAppDatabase::class.java,
                    "wearable_app_db"
                )
                    .fallbackToDestructiveMigration()
                    .enableMultiInstanceInvalidation()
                    .addTypeConverter(EcgConverter())
                    .build()
                INSTANCE = instance
                instance
            }

        fun getInstance(): WearableAppDatabase? = INSTANCE
    }
}
