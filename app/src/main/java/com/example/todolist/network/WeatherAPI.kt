package com.example.todolist.network

import com.example.todolist.Model.AstronomyInfo
import retrofit2.http.Query
import retrofit2.http.GET
import com.example.todolist.Model.WeatherData


interface WeatherAPI{
    @GET("v1/current.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String

    ):WeatherData

    @GET("v1/astronomy.json")
    suspend fun getAstronomy(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("dt") date: String
    ):AstronomyInfo
}

