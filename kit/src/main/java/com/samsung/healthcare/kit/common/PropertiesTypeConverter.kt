package com.samsung.healthcare.kit.common

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.samsung.healthcare.kit.entity.Task.Properties
import com.samsung.healthcare.kit.external.data.ItemProperties
import com.samsung.healthcare.kit.external.network.util.PropertyDeserializer

@ProvidedTypeConverter
class PropertiesTypeConverter() {

    private val gson: Gson = Gson()

    private val gsonWithConverter: Gson = GsonBuilder()
        .registerTypeAdapter(ItemProperties::class.java, PropertyDeserializer())
        .create()

    @TypeConverter
    fun objectToJson(value: Properties): String = gson.toJson(value)

    @TypeConverter
    fun jsonToObject(value: String): Properties = gsonWithConverter.fromJson(value, Properties::class.java)
}
