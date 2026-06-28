package com.vibhanshu.wrbtechassessment.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): WeatherDto

    @GET("data/2.5/forecast")
    suspend fun getForecastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): ForecastDto

    @GET("data/2.5/air_pollution")
    suspend fun getAirPollutionData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): AirPollutionDto

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}
