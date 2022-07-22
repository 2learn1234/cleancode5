package com.example.androidcleanarchitecture.repository

import android.content.Context
import com.example.androidcleanarchitecture.database.SchoolRepository
import com.example.androidcleanarchitecture.di.NetworkModule
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.School
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import java.io.IOException

class DataRepository(var networkModule: NetworkModule, var context: Context?) :
    SchoolRepository() {
    // https://data.cityofnewyork.us/resource/f9bf-2cp4.json

    suspend fun getNewsFromNetwork(category: String): Flow<Response<List<School>>> {
        return flow<Response<List<School>>> {
            val response = networkModule.sourceOfNetwork().getNews()
            emit(response)
        }
    }

    suspend fun getNewsFromNetwork2(dbn: String): Flow<Response<List<SATScores>>> {
        return flow<Response<List<SATScores>>> {
            val response = networkModule.sourceOfNetwork().getNews2(dbn)
            emit(response)
        }
    }

}