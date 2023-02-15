package com.udacity.asteroidradar.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.udacity.asteroidradar.model.DatabaseAsteroid
import com.udacity.asteroidradar.model.PictureOfDay

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAsteroid( vararg asteroid: DatabaseAsteroid)

    @Query ("SELECT * FROM asteroids_table ORDER BY closeApproachDate")
     fun getAllAsteroids() : LiveData<List<DatabaseAsteroid>>

    @Query ("SELECT * FROM asteroids_table WHERE closeApproachDate = :date ORDER BY closeApproachDate")
    fun getTodayAsteroids(date: String) : LiveData<List<DatabaseAsteroid>>

    @Query ("SELECT * FROM asteroids_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate")
    fun getWeekAsteroids(startDate :String, endDate :String) : LiveData<List<DatabaseAsteroid>>

    @Query ( "DELETE FROM asteroids_table WHERE closeApproachDate < :date")
     fun delete(date: String)

}

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture( pictureOfDay: PictureOfDay)

    @Query ("SELECT * FROM picture_of_day")
    fun getPicture() : LiveData<PictureOfDay>

    @Query ( "DELETE FROM picture_of_day WHERE date < :date ")
    fun deletePicture(date: String)

}