package com.vibhanshu.wrbtechassessment.domain.repository

import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherData(
        lat: Double,
        lon: Double,
        fetchFromRemote: Boolean
    ): Flow<Resource<WeatherInfo>>

    fun getWeatherByCity(
        cityName: String
    ): Flow<Resource<WeatherInfo>>

    suspend fun getLatestCachedWeather(): WeatherInfo?
}
