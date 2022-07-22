package com.example.androidcleanarchitecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.network.ResponseModel
import com.example.androidcleanarchitecture.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SchoolsViewModel(public var dataRepo: DataRepository) : ViewModel() {

    val schoolData =
        MutableStateFlow<ResponseModel<Response<List<School>>>>(ResponseModel.Idle("Idle State"))
    val category = MutableStateFlow<String>("")
    val newsURL = MutableStateFlow<String>("")

    val schoolInfoData =
        MutableStateFlow<ResponseModel<Response<List<SATScores>>>>(ResponseModel.Idle("Idle State"))
    val newsURL2 = MutableStateFlow<String>("")


    suspend fun getNews(value: String) {
       // schoolData.emit(ResponseModel.Loading())
        dataRepo.getNewsFromNetwork(value).collect {
            viewModelScope.launch {
                if (it.isSuccessful) {
                    schoolData.emit(ResponseModel.Success(it))
                    dataRepo.insertAll(it.body())
                } else
                    schoolData.emit(ResponseModel.Error(it.message()))
            }
        }
    }

    suspend fun getNews2(value: String) {
        schoolInfoData.emit(ResponseModel.Loading())
        dataRepo?.getNewsFromNetwork2(value)?.collect {
            viewModelScope.launch {
                if (it.isSuccessful) {
                    schoolInfoData.emit(ResponseModel.Success(it))
                    dataRepo.insertAllScores(it.body())
                } else
                    schoolInfoData.emit(ResponseModel.Error(it.message()))
            }
        }
    }


    suspend fun transmitCategory(value: String) {
        category.emit(value)
    }

    suspend fun transmitNewsURL(value: String) {
        newsURL.emit(value)
    }

}