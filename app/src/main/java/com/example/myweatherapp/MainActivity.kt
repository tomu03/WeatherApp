package com.example.myweatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.myweatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCity = findViewById<EditText>(R.id.etCity)
        val btnGetWeather = findViewById<Button>(R.id.btnGetWeather)
        val tvCity = findViewById<TextView>(R.id.tvCity)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvCondition = findViewById<TextView>(R.id.tvCondition)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvWindSpeed = findViewById<TextView>(R.id.tvWindSpeed)
        val tvSunrise = findViewById<TextView>(R.id.tvSunrise)
        val tvSunset = findViewById<TextView>(R.id.tvSunset)
        val ivWeatherIcon = findViewById<ImageView>(R.id.ivWeatherIcon)

        btnGetWeather.setOnClickListener {
            val city = etCity.text.toString()
            if (city.isNotEmpty()) {
                viewModel.fetchWeather(city)
            }
        }

        viewModel.weatherData.observe(this) { weather ->
            weather?.let {
                tvCity.text = it.name
                tvTemperature.text = "Temperature: ${it.main.temp}°C"
                tvCondition.text = it.weather[0].description
                tvHumidity.text = "Humidity: ${it.main.humidity}%"
                tvWindSpeed.text = "Wind Speed: ${it.wind.speed} m/s"
                tvSunrise.text = "Sunrise: ${formatTime(it.sys.sunrise)}"
                tvSunset.text = "Sunset: ${formatTime(it.sys.sunset)}"
                ivWeatherIcon.load("https://openweathermap.org/img/wn/${it.weather[0].icon}@2x.png")
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            tvCity.text = error
            tvTemperature.text = ""
            tvCondition.text = ""
            tvHumidity.text = ""
            tvWindSpeed.text = ""
            tvSunrise.text = ""
            tvSunset.text = ""
            ivWeatherIcon.setImageDrawable(null)
        }
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000)) // Convert seconds to milliseconds
    }
}