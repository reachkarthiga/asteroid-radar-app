package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") key :String) : PictureOfDay
}

object PictureOfTheDayCall {
    val retrofitService : PictureOfTheDayAPI by lazy {
        retrofit.create(PictureOfTheDayAPI::class.java)
    }
}

