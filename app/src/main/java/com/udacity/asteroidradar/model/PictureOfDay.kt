package com.udacity.asteroidradar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "picture_of_day")
data class PictureOfDay
    (
    @PrimaryKey(autoGenerate = false)
    val date: String,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
) {
}