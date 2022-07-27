package com.example.androidcleanarchitecture.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.network.ResponseModel
import com.example.androidcleanarchitecture.repository.DataRepository
import com.example.androidcleanarchitecture.utils.UiState
import com.example.androidcleanarchitecture.utils.foldApiStates
import com.hadiyarajesh.flower.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class SchoolsViewModel(public var dataRepo: DataRepository) : ViewModel() {

    companion object {
        const val TAG = "SchoolsViewModel"
    }

    private val _myData: MutableStateFlow<UiState<List<School>>> = MutableStateFlow(UiState.Empty)
    val myData: StateFlow<UiState<List<School>>> = _myData

    val currentPageNo: MutableLiveData<Int> = MutableLiveData(1)

    private val commandsChannel = Channel<Command>()
    private val commands = commandsChannel.receiveAsFlow()

    private val _searchSchools_ = MutableStateFlow<List<School>>(emptyList())
    val searchSchools: StateFlow<List<School>> = _searchSchools_

    init {
        changePage(1)
    }

    val quotes = commands.flatMapLatest { command ->
        flow {
            when (command) {
                is Command.ChangePageCommand -> {
                    getSchoolsForFragment(command.page).foldApiStates({ quote ->
                        delay(250)
                        emit(State.UIState(quote, currentPageNo.value ?: 1))
                    }, { emit(it) }, { emit(it) })
                }
            }

        }
    }.asLiveData(viewModelScope.coroutineContext)

    public fun changePage(page: Int) {
        viewModelScope.launch {
            currentPageNo.value = page
            commandsChannel.send(Command.ChangePageCommand(page))
        }
    }

    val schoolData = MutableStateFlow<ResponseModel<Response<List<School>>>>(ResponseModel.Idle("Idle State"))



    //val schoolData:Immu
    val category = MutableStateFlow<String>("")
    val newsURL = MutableStateFlow<String>("")

    val schoolInfoData =
        MutableStateFlow<ResponseModel<Response<List<SATScores>>>>(ResponseModel.Idle("Idle State"))
    val newsURL2 = MutableStateFlow<String>("")

    // val allSchools=dataRepo.schools

    fun insertSchools(schools: List<School>) = viewModelScope.launch {
        dataRepo.insertAll(schools)
    }

    fun deleteSchools() = viewModelScope.launch {
        dataRepo.deleteShools()
    }

    fun deleteScores() = viewModelScope.launch {
        dataRepo.deleteScores()
    }

    fun searchSchools(searchQuery: String) = viewModelScope.launch {
        dataRepo.getSearchSchools(searchQuery).collect { schoolList ->
            _searchSchools_.emit(schoolList)
        }
    }

    private val getSchoolsForFragment ={ page: Int ->
        dataRepo.getRandomQuote(1,onFailed = { errorBody, statusCode ->
            Log.i("getRandomQuote", "onFailure => $errorBody ,$statusCode")
            viewModelScope.launch {
               // errorBody?.let { oneShotEventsChannel.send(Event.Error(it)) }
            }
            currentPageNo.postValue(currentPageNo.value?.minus(1))
        })
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
                      dataRepo.insertAll(it.body())
                } else
                    schoolData.emit(ResponseModel.Error(it.message()))
                  //  getMyData()
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

    fun getMyData(pageNo: Int, onFailed: (String?,Int) -> Unit = { _: String?, _: Int -> }): Flow<Resource<List<School>>> {
        return com.hadiyarajesh.flower.networkBoundResource(

            fetchFromLocal = {
                Log.i(TAG, "Fetching from local cache")
                val localResult = dataRepo.getSchools()
                localResult
            },
            shouldFetchFromRemote = {
                Log.i(TAG, "Checking if remote fetch is needed")
                it == null
            },
            fetchFromRemote = {
                Log.i(TAG, "Fetching from remote server")
                dataRepo.networkModule.sourceOfNetwork().getSchools3()

            },
            processRemoteResponse = { },
            saveRemoteData = { schools: List<School> ->
                Log.i(TAG, "Saving from remote data to local cache")
                dataRepo.insertAll(schools)
            },
            onFetchFailed = { _, _ -> }
            //  onFetchFailed { _: String?, _: Int ->}
        ).flowOn(Dispatchers.IO)
    }

    sealed class State {
        data class UIState(val quote: List<School>, val currentPage: Int) : State()
        data class SuccessState(val resource: Resource<*>) : State()
        data class ErrorState(val errorMessage: String, val statusCode: Int) : State()
        data class LoadingState(val loading: Boolean = true) : State()
    }


    sealed class Command {
        data class ChangePageCommand(val page: Int) : Command()
    }

}

