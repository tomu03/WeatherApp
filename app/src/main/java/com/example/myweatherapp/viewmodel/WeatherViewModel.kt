package com.example.myweatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.model.WeatherResponse
import com.example.myweatherapp.network.RetrofitClient
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val weatherData = MutableLiveData<WeatherResponse?>()
    val errorMessage = MutableLiveData<String>()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getWeather(city)
                weatherData.postValue(response)
            } catch (e: Exception) {
                errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}