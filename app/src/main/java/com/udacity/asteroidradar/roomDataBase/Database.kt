package com.udacity.asteroidradar.roomDataBase

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.DatabaseAsteroid


@androidx.room.Database(entities = [DatabaseAsteroid::class] , version = 1)
abstract class Database : RoomDatabase() {

    abstract val dao:Dao

    companion object{
        @Volatile
        private var INSTANCE : com.udacity.asteroidradar.roomDataBase.Database? = null

        fun getInstance(context: Context) : com.udacity.asteroidradar.roomDataBase.Database {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(context.applicationContext,
                        Database::class.java, "aster_database")
                        .fallbackToDestructiveMigration().build()

                    INSTANCE = instance

                }

                return instance
            }

        }

    }

}