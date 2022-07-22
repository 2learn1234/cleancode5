package com.example.androidcleanarchitecture.network

import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.School
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET(URL.GET_NEWS)
    public suspend fun getNews(
    ) : Response<List<School>>

    @GET(URL.GET_NEWS2)
    public suspend fun getNews2(@Query("dbn") dbn: String): Response<List<SATScores>>

}