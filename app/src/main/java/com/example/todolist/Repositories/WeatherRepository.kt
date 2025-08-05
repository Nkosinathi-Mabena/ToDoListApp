package com.example.todolist.Repositories

import com.example.todolist.Model.AstronomyInfo
import com.example.todolist.Model.WeatherData
import com.example.todolist.network.WeatherAPI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface WeatherRepository{
    suspend fun getWeatherInfo(): WeatherData
    suspend fun getAstronomyInfo(): AstronomyInfo
}

class NetworkWeatherRepository(private val weatherApi: WeatherAPI): WeatherRepository{
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = formatter.format(Date())
    override suspend fun getWeatherInfo(): WeatherData = weatherApi.getWeather(apiKey = "43905c745e8b4403990113115252306",location = "Sandton")
    override suspend fun getAstronomyInfo(): AstronomyInfo = weatherApi.getAstronomy(apiKey = "43905c745e8b4403990113115252306", location = "Sandton", date = currentDate)

}