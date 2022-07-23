package com.example.androidcleanarchitecture.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidcleanarchitecture.database.SchoolRoomDatabase
import com.example.androidcleanarchitecture.di.NetworkModule
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.SATScoresDao
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.model.SchoolDao
import com.example.androidcleanarchitecture.network.URL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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


    suspend fun insertAll(schools:List<School>)=mSchoolDao.insertAll(schools)

    fun getSchools()=mSchoolDao.selectSchools()

    fun seachSchools(searchQuery: String?)=mSchoolDao.searchInSchoolTable(searchQuery)
    suspend fun deleteAllSchools()=mSchoolDao.deleteAll()


    /**
     * Search in DB
     * @param searchString
     * @return
     */
    public fun getSearchSchools(searchString: String?): Flow<List<School>> {
        return mSchoolDao.searchInSchoolTable(searchString)
    }



    /**
     * Insert Schools into DB in background
     * @param schools
     */
    fun insertAll(schools: List<School>?) {
        SchoolRoomDatabase.databaseWriteExecutor.execute {
            mSchoolDao.insertAll(
                schools
            )
        }
    }

    /**
     * Insert Scores into DB in background
     * @param scores
     */
    public fun insertAllScores(scores: List<SATScores>?) {
        SchoolRoomDatabase.databaseWriteExecutor.execute {
            mScoresDao.insertAll(
                scores
            )
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

    suspend fun deleteShools() {
        mSchoolDao.deleteAll()
    }

    suspend fun deleteScores() {
        mScoresDao.deleteAll()
    }


}