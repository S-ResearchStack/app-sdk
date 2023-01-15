package healthstack.backend.integration.adapter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import healthstack.backend.integration.task.PropertyDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val interceptor = HttpLoggingInterceptor()

    fun <T> create(url: String, serviceClass: Class<T>): T {
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(
                healthstack.backend.integration.task.ItemProperties::class.java,
                PropertyDeserializer()
            )
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
