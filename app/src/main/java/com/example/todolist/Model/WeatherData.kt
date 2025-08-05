package com.example.todolist.Model

//import android.location.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class WeatherData(
    val location: LocationData,
    val current: Current,
)

@Serializable
data class AstronomyInfo(
    val location: LocationData,
    val astronomy: AstronomyDetail
)

@Serializable
data class AstronomyDetail(
    val astro: Astronomy
)

@Serializable
data class Astronomy(
    val sunrise: String,
    val sunset: String
)

@Serializable
data class LocationData(
    val name: String,
    val region: String,
    val country: String,
    val localtime: String
)

@Serializable
data class Current(
    @SerialName("temp_c") val temp: Double
)