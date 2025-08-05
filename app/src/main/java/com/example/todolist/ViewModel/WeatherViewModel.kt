package com.example.todolist.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.todolist.Model.AstronomyInfo
import com.example.todolist.Model.WeatherData
import com.example.todolist.Repositories.NetworkWeatherRepository
import com.example.todolist.Repositories.WeatherRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.example.todolist.network.WeatherAPI
import okhttp3.MediaType.Companion.toMediaType
import okio.IOException
import retrofit2.Retrofit
import retrofit2.Retrofit.*

sealed interface WeatherUiState{
    data class Successs(val weatherInfo: WeatherData, val astronomyInfo: AstronomyInfo): WeatherUiState
    object Error: WeatherUiState // Error screen
    object Load: WeatherUiState // Loading screen
}
class WeatherViewModel(private val networkWeatherRepository: NetworkWeatherRepository): ViewModel(){

    private val BASE_URL = "https://api.weatherapi.com/"

    private val json = Json {
        ignoreUnknownKeys = true //
    }

    private val retrofit: Retrofit = Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Load)
        private set

   private val retrofitService: WeatherAPI by lazy {
       retrofit.create(WeatherAPI::class.java)
   }

    val weatherRepository: WeatherRepository by lazy {
        NetworkWeatherRepository(retrofitService)
    }


    init {
        getWeatherTemp()
    }

    fun getWeatherTemp(){
        viewModelScope.launch {
            weatherUiState = WeatherUiState.Load
            weatherUiState = try{
                WeatherUiState.Successs(
                    networkWeatherRepository.getWeatherInfo(),
                    networkWeatherRepository.getAstronomyInfo()
                )
            } catch (e: IOException) {
                Log.i("api error", "$e")
                WeatherUiState.Error
            } catch (e: HttpException) {
                Log.i("api erro", "$e")
                WeatherUiState.Error
            }

        }
    }

}
class WeatherViewModelProvider(private val repository: NetworkWeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}
