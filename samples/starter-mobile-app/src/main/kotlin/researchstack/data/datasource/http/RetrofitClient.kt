package researchstack.data.datasource.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES

class RetrofitClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(1, MINUTES)
                    .readTimeout(1, MINUTES)
                    .writeTimeout(@Suppress("MagicNumber") 10, MINUTES)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAPI(): FileUploadApi {
        return retrofit.create(FileUploadApi::class.java)
    }

    companion object {
        // TODO - base url as what?
        private const val BASE_URL = "https://127.0.0.1"
        private lateinit var INSTANCE: RetrofitClient

        fun initialize() {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = RetrofitClient()
                }
            }
        }

        fun getInstance(): RetrofitClient = INSTANCE
    }
}
