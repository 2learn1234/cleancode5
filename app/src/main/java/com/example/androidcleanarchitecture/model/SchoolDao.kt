package com.example.androidcleanarchitecture.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAll(school: List<School?>?)


    @Query("DELETE FROM school_table")
    fun deleteAll()


    @get:Query("SELECT * FROM school_table ORDER BY school_name ASC")
    @get:Transaction
    val schools: LiveData<List<School>>

    @Transaction
    @Query("SELECT * FROM school_table where school_name like :searchString ORDER BY school_name ASC")
     fun getSchoolsFiltered(searchString: String?): LiveData<List<School>>

}
