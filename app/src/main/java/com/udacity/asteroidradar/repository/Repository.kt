package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.asDomainModel
import com.udacity.asteroidradar.model.toDataBaseModel
import com.udacity.asteroidradar.network.AsteroidCall
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.roomDataBase.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Repository( val database: Dao) {

//    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.getAllAsteroids()) {
//        it.asDomainModel()
//    }

    suspend fun getAsteroidDetails() {

        val today: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dateAfter1Week = Calendar.getInstance()
        dateAfter1Week.add(Calendar.DATE, 1)

        withContext(Dispatchers.IO) {
            val networkData = AsteroidCall
                .retrofitService
                .getAsteroidsList(
                    sdf.format(today.time).toString(),
                    sdf.format(dateAfter1Week.time).toString(),
                    "MmTVrTp2EogMQqPKK6ZVAUP5u3gcT6mICPQZmAyv"
                )

            val networkAsteroid  = (parseAsteroidsJsonResult(JSONObject(networkData))).toDataBaseModel()
            database.insertAsteroid(*networkAsteroid.toTypedArray())

            val test1 = database.getAllAsteroids()
            Log.i("test", test1.toString())

        }
    }
}