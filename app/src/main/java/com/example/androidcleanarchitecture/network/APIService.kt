package com.example.androidcleanarchitecture.network

import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.School
import com.hadiyarajesh.flower.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET(URL.GET_SCHOOLS)
    public suspend fun getSchools(
    ) : Response<List<School>>
    @GET(URL.GET_SCORES)
    public suspend fun getScores(@Query("dbn") dbn: String): Response<List<SATScores>>


    @GET(URL.GET_SCHOOLS)
    public fun getSchools3() : Flow<ApiResponse<List<School>>>


}