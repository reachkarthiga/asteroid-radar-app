package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.asDomainModel
import com.udacity.asteroidradar.model.toDataBaseModel
import com.udacity.asteroidradar.network.AsteroidCall
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.roomDataBase.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Repository( val database: Dao) {

    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.getAllAsteroids()) {
        it.asDomainModel()
    }

    suspend fun getAsteroidDetails() {

        val today: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var endDate:String = ""

        today.add(Calendar.DATE, 7)
        endDate = sdf.format(today.time).toString()

        withContext(Dispatchers.IO) {
            val test = AsteroidCall
                .retrofitService
                .getAsteroidsList(
                    sdf.format(today.time),
                    endDate,
                    "MmTVrTp2EogMQqPKK6ZVAUP5u3gcT6mICPQZmAyv"
                )

            val network  = (parseAsteroidsJsonResult(JSONObject(test)))
            database.insertAsteroid(network.toDataBaseModel())

            today.time = Calendar.getInstance().time
            database.delete(sdf.format(today.time).toString())

        }
    }
}