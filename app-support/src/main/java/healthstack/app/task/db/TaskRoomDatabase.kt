package healthstack.app.task.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import healthstack.app.task.converter.LocalDateTimeConverter
import healthstack.app.task.converter.PropertiesTypeConverter
import healthstack.app.task.converter.ResultConverter
import healthstack.app.task.dao.TaskDao

@Database(entities = [healthstack.app.task.entity.Task::class], version = 1, exportSchema = false)
@TypeConverters(
    value = [
        PropertiesTypeConverter::class,
        LocalDateTimeConverter::class,
        ResultConverter::class,
    ]
)
abstract class TaskRoomDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {

        private lateinit var INSTANCE: TaskRoomDatabase

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = Room.databaseBuilder(context, TaskRoomDatabase::class.java, "task")
                        .addTypeConverter(PropertiesTypeConverter())
                        .addTypeConverter(LocalDateTimeConverter())
                        .addTypeConverter(ResultConverter(Gson()))
                        .build()
                }
            }
        }

        fun getInstance(): TaskRoomDatabase = INSTANCE
    }
}
