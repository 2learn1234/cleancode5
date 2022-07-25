package com.example.androidcleanarchitecture.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(school: List<School?>?)

    @Insert
    suspend fun insertSchool(school: School)=null

    @Delete
    suspend fun deleteSchool(school: School)=null

    @Query("DELETE FROM school_table")
    suspend fun deleteAll()=null

    @Query("SELECT * FROM school_table ORDER BY dbn DESC")
    fun selectSchools():Flow<List<School>>

    @Query("SELECT * FROM school_table WHERE school_name LIKE '%' ||:searchQuery||'%'")
    fun searchInSchoolTable(searchQuery:String?):Flow<List<School>>


    @get:Query("SELECT * FROM school_table ORDER BY school_name DESC")
    @get:Transaction
    val schools: Flow<List<School>>

  /*  @Transaction
    @Query("SELECT * FROM school_table where school_name like :searchString ORDER BY school_name ASC")
    fun getSchoolsFiltered(searchString: String?): LiveData<List<School>>*/

}
