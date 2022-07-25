package com.example.androidcleanarchitecture.database


import android.content.Context
import androidx.lifecycle.LiveData
import com.example.androidcleanarchitecture.model.SATScores
import com.example.androidcleanarchitecture.model.SATScoresDao
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.model.SchoolDao
import com.example.androidcleanarchitecture.network.URL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import okhttp3.*

open class SchoolRepository(val context: Context?) {//internal constructor(context) {
    //private val mSchoolDao: SchoolDao
    //private val mScoresDao: SATScoresDao

    /**
     * Will fetch Schools list as LiveData so that it can be executed in the background
     * @return
     */
    // public val allSchools: LiveData<List<School>>

    /**
     * Search in DB
     * @param searchString
     * @return
     */
    /*  public fun getFilteredSchools(searchString: String?): LiveData<List<School>> {
          return mSchoolDao.getSchoolsFiltered(searchString)
      }*/

    /*
     * Get SATScores for School DBN
     * @param schoolDBN
     * @return
     */
/*    public open fun getSATScoresForSchool(schoolDBN: String?): LiveData<SATScores>? {
        return mScoresDao?.getScore(schoolDBN)
    }*/

    /*  */
    /**
     * Insert Schools into DB in background
     * @param schools
     *//*
    fun insertAll(schools: List<School>?) {
        SchoolRoomDatabase.databaseWriteExecutor.execute {
            mSchoolDao.insertAll(
                schools
            )
        }
    }

    */
    /**
     * Insert Scores into DB in background
     * @param scores
     *//*
    public fun insertAllScores(scores: List<SATScores>?) {
        SchoolRoomDatabase.databaseWriteExecutor.execute {
            mScoresDao.insertAll(
                scores
            )
        }
    }

    */
    /**
     * From here lies all code related to REST API calls using OKHTTP.
     * We can put them in another class to handle them.
     *//*
    public fun loadSchools() {
        fetchSchoolsData()
        fetchSATScores()
    }

    */
    /**
     * Fetches School Data from NYC Schools API
     *//*
    public  fun fetchSchoolsData() {
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
                override fun onResponse(call: Call, response: Response) {
                    val jsonData = response.body!!.string()
                    // Load data as School Object using Gson
                    val listType = object : TypeToken<List<School?>?>() {}.type
                    val schools = Gson().fromJson<List<School>>(jsonData, listType)
                    insertAll(schools)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    */
    /**
     * Fetches SATScores Data from NYC Schools API
     *//*
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
                override fun onResponse(call: Call, response: Response) {
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

    companion object {

        @Volatile
        private var INSTANCE: SchoolRepository? = null

        */
    /**
     * Singleton Instance
     * @param context
     * @return
     *//*
        *//*public fun getRepository(context: Context?): SchoolRepository? {
            if (INSTANCE == null) {
                synchronized(SchoolRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = SchoolRepository(context)
                    }
                }
            }
            return INSTANCE
        }*//*
    }*/

    /*   init {
           val db = SchoolRoomDatabase.getDatabase(context = context)
           mSchoolDao = db.schoolDao()
           mScoresDao = db.satScoresDao()
          allSchools = mSchoolDao.schools
       }*/
}