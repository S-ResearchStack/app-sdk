package com.samsung.healthcare.kit.external.network.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.samsung.healthcare.kit.external.data.ItemProperties
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val interceptor = HttpLoggingInterceptor()

    fun <T> create(url: String, serviceClass: Class<T>): T {
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(ItemProperties::class.java, PropertyDeserializer())
            .create()

        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(url)
            .build()
            .create(serviceClass)
    }
}
