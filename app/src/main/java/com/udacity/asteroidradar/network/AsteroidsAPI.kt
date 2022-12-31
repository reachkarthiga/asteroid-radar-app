package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidsAPI {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsList(@Query("start_date") startDate :String,
                                 @Query("end_date") endDate :String,
                                 @Query("api_key") key :String) : String
}

object AsteroidCall {
    val retrofitService : AsteroidsAPI by lazy {
        retrofit.create(AsteroidsAPI::class.java)
    }
}

