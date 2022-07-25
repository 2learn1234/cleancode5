package com.example.androidcleanarchitecture.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.network.APIService
import com.example.androidcleanarchitecture.network.ResponseModel
import com.example.androidcleanarchitecture.repository.DataRepository
import com.hadiyarajesh.flower.Resource
import com.hadiyarajesh.flower.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

import kotlinx.coroutines.flow.flowOn


class SchoolsViewModel(public var dataRepo: DataRepository) : ViewModel() {

    val schoolData =
        MutableStateFlow<ResponseModel<Response<List<School>>>>(ResponseModel.Idle("Idle State"))

    private  val _searchSchools_ = MutableStateFlow<List<School>>(emptyList())
    val searchSchools:StateFlow<List<School>> = _searchSchools_

    //val schoolData:Immu
    val category = MutableStateFlow<String>("")
    val newsURL = MutableStateFlow<String>("")

    val schoolInfoData =
        MutableStateFlow<ResponseModel<Response<List<SATScores>>>>(ResponseModel.Idle("Idle State"))
    val newsURL2 = MutableStateFlow<String>("")

   // val allSchools=dataRepo.schools

    fun insertSchools(schools:List<School>)=viewModelScope.launch {
        dataRepo.insertAll(schools)
    }

   fun deleteSchools()=viewModelScope.launch {
       dataRepo.deleteShools()
   }

    fun deleteScores()=viewModelScope.launch {
        dataRepo.deleteScores()
    }

    fun searchSchools(searchQuery: String)=viewModelScope.launch {
        dataRepo.getSearchSchools(searchQuery).collect{  schoolList ->
            _searchSchools_.emit(schoolList)
        }
    }

    sealed class State {
        data class UIState(val quote: School, val currentPage: Int) : State()
        data class SuccessState(val resource: Resource<*>) : State()
        data class ErrorState(val errorMessage: String, val statusCode: Int) : State()
        data class LoadingState(val loading: Boolean = true) : State()
    }

    sealed class Event {
        data class Error(val message: String) : Event()
    }


    suspend fun getNews(value: String) {
       // schoolData.emit(ResponseModel.Loading())
        dataRepo.getNewsFromNetwork(value).collect {
            viewModelScope.launch {
                if (it.isSuccessful) {
                    schoolData.emit(ResponseModel.Success(it))
                  //  dataRepo.insertAll(it.body())
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
    fun getMyData(): Flow<Resource<List<School>>> {
        return com.hadiyarajesh.flower.networkBoundResource(
            fetchFromLocal = { dataRepo.getSchools() },
            shouldFetchFromRemote = { Log.i("TAG", "Fetching from local cache")
              it==null },
            fetchFromRemote = {
                dataRepo.networkModule.sourceOfNetwork().getSchools3()

            },
            processRemoteResponse = { },
            saveRemoteData = { schools:List<School> ->
                dataRepo.insertAll2(schools) },
            onFetchFailed = {_,_ ->}
          //  onFetchFailed { _: String?, _: Int ->}
        ).flowOn(Dispatchers.IO)
    }

}

