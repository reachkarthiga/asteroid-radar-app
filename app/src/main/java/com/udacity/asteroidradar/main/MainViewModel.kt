package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.model.*
import com.udacity.asteroidradar.roomDataBase.Dao
import com.udacity.asteroidradar.roomDataBase.PictureDao

enum class mainPageFilter( val filterType :Int) {
    TODAY(1),
    WEEK(2),
    SHOW_ALL(0)
}

class MainViewModel() : ViewModel() {

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid>
        get() = _navigateToDetail

    private val _filterType = MutableLiveData<Int>()
    val filterType: LiveData<Int>
        get() = _filterType

    fun onAsteroidItemClick(id: Asteroid) {
        _navigateToDetail.value = id
    }

    fun doneDetailNavigation() {
        _navigateToDetail.value = null
    }

    init {
        showAllAsteroids()
    }

    fun todayFilter() {
        _filterType.value = mainPageFilter.TODAY.filterType
    }

    fun weekFilter() {
        _filterType.value = mainPageFilter.WEEK.filterType
    }

    fun showAllAsteroids() {
        _filterType.value = mainPageFilter.SHOW_ALL.filterType
    }

}