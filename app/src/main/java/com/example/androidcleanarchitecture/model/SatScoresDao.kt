package com.example.androidcleanarchitecture.model


import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface SATScoresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(scores: List<SATScores?>?)

    @Query("DELETE FROM sat_table")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM sat_table where dbn = :schoolDBN")
    fun getScore(schoolDBN: String?): LiveData<SATScores>

    @get:Query("SELECT * FROM sat_table  ORDER BY school_name ASC")
    @get:Transaction
    @get:VisibleForTesting
    val allScores:Flow<List<SATScores?>?>?
}
