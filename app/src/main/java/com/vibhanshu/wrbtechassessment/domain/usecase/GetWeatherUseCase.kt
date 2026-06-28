package com.vibhanshu.wrbtechassessment.domain.usecase

import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(
        lat: Double,
        lon: Double,
        fetchFromRemote: Boolean
    ): Flow<Resource<WeatherInfo>> {
        return repository.getWeatherData(lat, lon, fetchFromRemote)
    }

    fun getWeatherByCity(cityName: String): Flow<Resource<WeatherInfo>> {
        return repository.getWeatherByCity(cityName)
    }

    suspend fun getLatestCachedWeather(): WeatherInfo? {
        return repository.getLatestCachedWeather()
    }
}
