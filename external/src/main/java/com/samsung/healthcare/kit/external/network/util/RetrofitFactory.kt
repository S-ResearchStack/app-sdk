package com.samsung.healthcare.kit.external.network.util

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val interceptor = HttpLoggingInterceptor()

    fun <T> create(url: String, serviceClass: Class<T>): T {
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
            .create(serviceClass)
    }
}
