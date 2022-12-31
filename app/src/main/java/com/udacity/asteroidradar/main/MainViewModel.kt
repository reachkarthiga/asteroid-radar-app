package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.model.*
import com.udacity.asteroidradar.network.AsteroidCall
import com.udacity.asteroidradar.network.PictureOfTheDayCall
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.Repository
import com.udacity.asteroidradar.roomDataBase.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(val dao: Dao) : ViewModel() {

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid>
        get() = _navigateToDetail

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val _asteroidsList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidsList

    val repository = Repository(dao)

    fun onAsteroidItemClick(id: Asteroid) {
        _navigateToDetail.value = id
    }

    fun doneDetailNavigation() {
        _navigateToDetail.value = null
    }

    init {
        getPictureOfTheDay()
        getAsteroidListFromDB()
    }

    fun getAsteroidListFromDB() {
        viewModelScope.launch {
            repository.getAsteroidDetails()
        }

       // _asteroidsList.value = repository.asteroids.value
    }

//    fun weekFilter() {
//        today.add(Calendar.DATE, 7)
//        endDate = sdf.format(today.time).toString()
//        getAsteroidListFromDB(sdf.format(today.time).toString(), endDate)
//    }
//
//    fun todayFilter() {
//        getAsteroidListFromDB(sdf.format(today.time).toString(), sdf.format(today.time).toString())
//    }

    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            _pictureOfTheDay.value = PictureOfTheDayCall
                .retrofitService
                .getPictureOfTheDay("MmTVrTp2EogMQqPKK6ZVAUP5u3gcT6mICPQZmAyv")
            Log.i("Result", _pictureOfTheDay.value.toString())
        }
    }

}