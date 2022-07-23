package com.example.androidcleanarchitecture.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "school_table")
data class School(
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @SerializedName("dbn")
    var dbn: String,

    @NonNull
    var school_name: String? = null,


    private var overview_paragraph: String? = null,
    private var phone_number: String? = null,
    private var school_email: String? = null,
    private var website: String? = null,
    private var total_students: String? = null,

    @ColumnInfo(name = "primary_address_line_1")
    private var primary_address_line_1: String? = null,

    @ColumnInfo(name = "city")
    private var city: String? = null,

    @ColumnInfo(name = "zip")
    private var zip: String? = null,

    @ColumnInfo(name = "latitude")
    private var latitude: String? = null

) : Parcelable {
    fun getOverview_paragraph(): String? {
        return overview_paragraph
    }

    fun setOverview_paragraph(overview_paragraph: String?) {
        this.overview_paragraph = overview_paragraph
    }

    fun getPhone_number(): String {
        return phone_number!!
    }

    fun setPhone_number(phone_number: String) {
        this.phone_number = phone_number
    }

    fun getSchool_email(): String? {
        return school_email
    }

    fun setSchool_email(school_email: String?) {
        this.school_email = school_email
    }

    fun getWebsite(): String? {
        return website
    }

    fun setWebsite(website: String?) {
        this.website = website
    }

    fun getTotal_students(): String {
        return total_students!!
    }

    fun setTotal_students(total_students: String) {
        this.total_students = total_students
    }

    fun getPrimary_address_line_1(): String {
        return primary_address_line_1!!
    }

    fun setPrimary_address_line_1(primary_address_line_1: String) {
        this.primary_address_line_1 = primary_address_line_1
    }

    fun getCity(): String {
        return city!!
    }

    fun setCity(city: String) {
        this.city = city
    }

    fun getZip(): String {
        return zip!!
    }

    fun setZip(zip: String) {
        this.zip = zip
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?) {
        this.longitude = longitude
    }

    @ColumnInfo(name = "longitude")
    private var longitude: String? = null


}