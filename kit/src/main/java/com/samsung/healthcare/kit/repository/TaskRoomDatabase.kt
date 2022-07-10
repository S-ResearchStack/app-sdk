package com.samsung.healthcare.kit.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.samsung.healthcare.kit.common.LocalDateTimeConverter
import com.samsung.healthcare.kit.common.PropertiesTypeConverter
import com.samsung.healthcare.kit.common.ResultConverter
import com.samsung.healthcare.kit.entity.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
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
