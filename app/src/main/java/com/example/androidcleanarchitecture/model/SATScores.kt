package com.example.androidcleanarchitecture.model

import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "sat_table")
class SATScores(
    @PrimaryKey(autoGenerate = true)
    var id: Long,


    @ColumnInfo(name = "dbn")
    var dbn: String,


    @ColumnInfo(name = "school_name")
    private var school_name: String,

    @ColumnInfo(name = "num_of_sat_test_takers")
    private var num_of_sat_test_takers: String,

    @ColumnInfo(name = "sat_critical_reading_avg_score")
    private var sat_critical_reading_avg_score: String,

    @ColumnInfo(name = "sat_math_avg_score")
    private var sat_math_avg_score: String,

    @ColumnInfo(name = "sat_writing_avg_score")
    private var sat_writing_avg_score: String

    ) : Parcelable {

    fun getSchool_name(): String {
        return school_name
    }

    fun setSchool_name(school_name: String) {
        this.school_name = school_name
    }

    fun getNum_of_sat_test_takers(): String {
        return num_of_sat_test_takers
    }

    fun setNum_of_sat_test_takers(num_of_sat_test_takers: String) {
        this.num_of_sat_test_takers = num_of_sat_test_takers
    }

    fun getSat_critical_reading_avg_score(): String {
        return sat_critical_reading_avg_score
    }

    fun setSat_critical_reading_avg_score(sat_critical_reading_avg_score: String) {
        this.sat_critical_reading_avg_score = sat_critical_reading_avg_score
    }

    fun getSat_math_avg_score(): String {
        return sat_math_avg_score
    }

    fun setSat_math_avg_score(sat_math_avg_score: String) {
        this.sat_math_avg_score = sat_math_avg_score
    }

    fun getSat_writing_avg_score(): String {
        return sat_writing_avg_score
    }

    fun setSat_writing_avg_score(sat_writing_avg_score: String) {
        this.sat_writing_avg_score = sat_writing_avg_score
    }


    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("No. of SAT Test Takers : ")
        sb.append(num_of_sat_test_takers)
        sb.append(System.getProperty("line.separator"))
        sb.append("SAT Critical Reading Average score : ")
        sb.append(sat_critical_reading_avg_score)
        sb.append(System.getProperty("line.separator"))
        sb.append("SAT Math Average score : ")
        sb.append(sat_math_avg_score)
        sb.append(System.getProperty("line.separator"))
        sb.append("SAT Writing Average score : ")
        sb.append(sat_writing_avg_score)
        return sb.toString()
    }

    init {
        dbn = ""
        school_name = ""
        num_of_sat_test_takers = ""
        sat_critical_reading_avg_score = ""
        sat_math_avg_score = ""
        sat_writing_avg_score = ""
    }
}
