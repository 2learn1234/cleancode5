package com.example.androidcleanarchitecture.network

import com.example.androidcleanarchitecture.BuildConfig

object URL {
     const val baseURL: String = BuildConfig.BASE_URL
     const val GET_NEWS = "s3k6-pzi2.json"
     const val GET_NEWS2 = "f9bf-2cp4.json"
      const val NYC_SCHOOLS_URL = "https://data.cityofnewyork.us/resource/s3k6-pzi2.json"
      const val NYC_SCHOOLS_SAT_SCORES_URL =
          "https://data.cityofnewyork.us/resource/f9bf-2cp4.json"
}