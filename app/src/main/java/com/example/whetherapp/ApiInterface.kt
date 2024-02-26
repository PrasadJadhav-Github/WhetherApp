package com.example.whetherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiInterface {
    @GET("weather")

    fun getWeatherData(
        @Query("q") city:String,
        @Query("appid")appid:String,
        @Query("units")unit: String
    ):Call<whetherapp>


}