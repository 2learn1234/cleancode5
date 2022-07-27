package com.example.androidcleanarchitecture.repository

import android.content.Context
import android.util.Log
import com.example.androidcleanarchitecture.database.SchoolRoomDatabase
import com.example.androidcleanarchitecture.di.NetworkModule
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.SATScoresDao
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.model.SchoolDao
import com.example.androidcleanarchitecture.network.URL
import com.example.androidcleanarchitecture.viewmodel.SchoolsViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadiyarajesh.flower.ApiResponse
import com.hadiyarajesh.flower.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import java.io.IOException

class DataRepository(var networkModule: NetworkModule, context: Context) {
    private var mSchoolDao: SchoolDao
    private var mScoresDao: SATScoresDao


    init {
        val db = SchoolRoomDatabase.getDatabaseInstance(context = context)
        mSchoolDao = db.schoolDao
        mScoresDao = db.satScoresDao
    }


    suspend fun insertAll(schools:List<School>?)=mSchoolDao.insertAll(schools)

    fun getSchools()=mSchoolDao.selectSchools()


    fun seachSchools(searchQuery: String?)=mSchoolDao.searchInSchoolTable(searchQuery)
    suspend fun deleteAllSchools()=mSchoolDao.deleteAll()


    /**
     * Search in DB
     * @param searchString
     * @return
     */
    fun getSearchSchools(searchString: String): Flow<List<School>> {
        return mSchoolDao.searchInSchoolTable(searchString)
    }



    /**
     * Insert Schools into DB in background
     * @param schools
     */
  /*  suspend fun insertAll2(schools: List<School>?) {
        SchoolRoomDatabase.databaseWriteExecutor.execute {
            try {
                mSchoolDao.insertAll(
                    schools
                )
            } catch (e: Exception) {
            }
        }
    }*/

    /**
     * Insert Scores into DB in background
     * @param scores
     */
    public fun insertAllScores(scores: List<SATScores>?) {
        SchoolRoomDatabase.databaseWriteExecutor.execute {
            try {
                mScoresDao.insertAll(
                    scores
                )
            } catch (e: Exception) {
            }
        }
    }

    /**
     * From here lies all code related to REST API calls using OKHTTP.
     * We can put them in another class to handle them.
     */
    public fun loadSchools() {
        fetchSchoolsData()
        fetchSATScores()
    }

    /**
     * Fetches School Data from NYC Schools API
     */
    public fun fetchSchoolsData() {
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(URL.NYC_SCHOOLS_URL)
            .method("GET", null)
            .build()
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: okhttp3.Response) {
                    val jsonData = response.body!!.string()
                    // Load data as School Object using Gson
                    val listType = object : TypeToken<List<School?>?>() {}.type
                    val schools = Gson().fromJson<List<School>>(jsonData, listType)
                //    insertAll(schools)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Fetches SATScores Data from NYC Schools API
     */
    public fun fetchSATScores() {
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(URL.NYC_SCHOOLS_SAT_SCORES_URL)
            .method("GET", null)
            .build()
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: okhttp3.Response) {
                    val jsonData = response.body!!.string()
                    // Load data as SATScores Object using Gson
                    val listType = object : TypeToken<List<SATScores?>?>() {}.type
                    val satScores = Gson().fromJson<List<SATScores>>(jsonData, listType)
                    insertAllScores(satScores)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

/*    companion object {

        @Volatile
        private var INSTANCE: DataRepository? = null

        */
    /**
     * Singleton Instance
     * @param context
     * @return
     *//*
        public fun getRepository(context: Context): DataRepository? {
            if (INSTANCE == null) {
                synchronized(SchoolRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = DataRepository(context)
                    }
                }
            }
            return INSTANCE
        }
    }*/



    suspend fun getNewsFromNetwork(category: String): Flow<Response<List<School>>> {

        return flow<Response<List<School>>> {
          //  val response = networkModule.sourceOfNetwork().getSchools3()
            val response2 = networkModule.sourceOfNetwork().getSchools()
            emit(response2)
        }
    }

    suspend fun getNewsFromNetwork2(dbn: String): Flow<Response<List<SATScores>>> {
        return flow<Response<List<SATScores>>> {
            val response = networkModule.sourceOfNetwork().getScores(dbn)
            emit(response)
        }
    }

    suspend fun deleteShools() {
        mSchoolDao.deleteAll()
    }

    suspend fun deleteScores() {
        mScoresDao.deleteAll()
    }

    fun getRandomQuote(schools: Int, onFailed: (String?, Int) -> Unit = { _: String?, _: Int -> }): Flow<Resource<List<School>>> {
        return com.hadiyarajesh.flower.networkBoundResource(

            fetchFromLocal = {
                Log.e(SchoolsViewModel.TAG, "Fetching from local cache")
                val localResult = getSchools()
                localResult


            },
            shouldFetchFromRemote = {
                Log.i(SchoolsViewModel.TAG, "Checking if remote fetch is needed")
                it == null || it.size==0
            },
            fetchFromRemote = {
                Log.i(SchoolsViewModel.TAG, "Fetching from remote server")
                networkModule.sourceOfNetwork().getSchools3()

            },
            processRemoteResponse = { },
            saveRemoteData = { schools: List<School> ->
                Log.i(SchoolsViewModel.TAG, "Saving from remote data to local cache")
                mSchoolDao.insertAll(schools)
            },
            onFetchFailed = { errorBody, statusCode -> onFailed(errorBody, statusCode) },            //  onFetchFailed { _: String?, _: Int ->}
        ).map {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Resource.loading(null)
                }
                Resource.Status.SUCCESS -> {
                    val quote = it.data
                    Resource.success(quote)
                }
                is Resource.Status.ERROR -> {
                    val error = it.status as Resource.Status.ERROR
                    Resource.error(error.message, error.statusCode, it.data)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}