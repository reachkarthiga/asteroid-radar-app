package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel :ViewModel() {

    private val helper_clicked = MutableLiveData<Boolean>()
    val helperClicked :LiveData<Boolean>
        get() = helper_clicked

    init {
        helper_clicked.value = false
    }

    fun onHelperButtonClick() {
        helper_clicked.value = true
    }

    fun onDoneShowingHelper() {
        helper_clicked.value = false
    }
}