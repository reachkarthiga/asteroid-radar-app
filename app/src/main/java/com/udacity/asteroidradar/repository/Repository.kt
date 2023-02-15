package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.asDomainModel
import com.udacity.asteroidradar.model.toDataBaseModel
import com.udacity.asteroidradar.network.AsteroidCall
import com.udacity.asteroidradar.network.PictureOfTheDayCall
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.roomDataBase.Dao
import com.udacity.asteroidradar.roomDataBase.PictureDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Repository(val dao: Dao, val pictureDao: PictureDao) {

    val today: Calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(dao.getAllAsteroids()) {
        it.asDomainModel()
    }

    val pictureOfTheDay = pictureDao.getPicture()

    suspend fun getAsteroidDetails() {

        val dateAfter1Week = Calendar.getInstance()
        dateAfter1Week.add(Calendar.DATE, 7)

        withContext(Dispatchers.IO) {
            val networkData = AsteroidCall
                .retrofitService
                .getAsteroidsList(
                    sdf.format(today.time).toString(),
                    sdf.format(dateAfter1Week.time).toString(),
                    "MmTVrTp2EogMQqPKK6ZVAUP5u3gcT6mICPQZmAyv"
                )

            Log.i("Network Results" , networkData)

            val networkAsteroid =
                (parseAsteroidsJsonResult(JSONObject(networkData))).toDataBaseModel()
            dao.insertAsteroid(*networkAsteroid.toTypedArray())

            dao.delete(sdf.format(today.time))

            pictureDao.deletePicture(sdf.format(today.time))

            val pictureOfTheDay = PictureOfTheDayCall
                .retrofitService
                .getPictureOfTheDay("MmTVrTp2EogMQqPKK6ZVAUP5u3gcT6mICPQZmAyv")

            Log.i("Network Results" , pictureOfTheDay.toString())

            pictureDao.insertPicture(pictureOfTheDay)

        }
    }
}