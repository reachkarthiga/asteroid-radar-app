package com.udacity.asteroidradar.roomDataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.DatabaseAsteroid

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAsteroid( asteroid:List<DatabaseAsteroid>)

    @Query ("SELECT * FROM asteroids_table")
     fun getAllAsteroids() :LiveData<List<DatabaseAsteroid>>

    @Query ( "DELETE FROM asteroids_table WHERE closeApproachDate < :date")
     fun delete(date: String)

}