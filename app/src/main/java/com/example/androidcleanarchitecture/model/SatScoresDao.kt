package com.example.androidcleanarchitecture.model


import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface SATScoresDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAll(scores: List<SATScores?>?)

    @Query("DELETE FROM sat_table")
    suspend fun deleteAll()=null

   // @Query("SELECT * FROM sat_table WHERE school_name LIKE '%' ||:searchQuery||'%'")
    //fun searchSatTable(searchQuery:String?):Flow<List<School>>

/*    @Transaction
    @Query("SELECT * FROM sat_table where dbn = :schoolDBN")
    fun getScore(schoolDBN: String?): LiveData<SATScores>*/


    @get:Query("SELECT * FROM sat_table  ORDER BY school_name ASC")
   // @get:Transaction
    val allScores:Flow<List<SATScores?>?>?
}
