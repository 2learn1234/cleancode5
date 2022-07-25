package com.example.androidcleanarchitecture.network


import com.example.androidcleanarchitecture.network.URL.baseURL
import com.hadiyarajesh.flower.calladpater.FlowCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        private  val BASE_URL = baseURL

        fun create(): APIService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
               .addCallAdapterFactory(FlowCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }

    }
}