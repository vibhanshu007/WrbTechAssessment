package com.vibhanshu.wrbtechassessment.data.repository

import com.google.gson.Gson
import com.vibhanshu.wrbtechassessment.BuildConfig
import com.vibhanshu.wrbtechassessment.core.util.Constants
import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.data.local.WeatherDatabase
import com.vibhanshu.wrbtechassessment.data.local.WeatherEntity
import com.vibhanshu.wrbtechassessment.data.mapper.toWeatherInfo
import com.vibhanshu.wrbtechassessment.data.remote.GoogleGeocodingApiService
import com.vibhanshu.wrbtechassessment.data.remote.WeatherApiService
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val googleApi: GoogleGeocodingApiService,
    private val db: WeatherDatabase,
    private val gson: Gson
) : WeatherRepository {

    private val dao = db.dao

    override fun getWeatherData(
        lat: Double,
        lon: Double,
        fetchFromRemote: Boolean
    ): Flow<Resource<WeatherInfo>> = flow {
        emit(Resource.Loading(true))

        val roundedLat = String.format(Locale.US, Constants.ROUNDED_COORDINATE_FORMAT, lat).toDouble()
        val roundedLon = String.format(Locale.US, Constants.ROUNDED_COORDINATE_FORMAT, lon).toDouble()
        val cityId = "${roundedLat},${roundedLon}"

        val initialData = try {
            dao.getWeatherByCitySync(cityId) ?: dao.getLatestWeatherSync()
        } catch (e: Exception) { null }
        
        if (initialData != null) {
            try {
                val weatherInfo = gson.fromJson(initialData.weatherDataJson, WeatherInfo::class.java)
                emit(Resource.Success(weatherInfo))
            } catch (e: Exception) { e.printStackTrace() }
        }

        if (fetchFromRemote) {
            try {
                val weatherResponse = api.getWeatherData(lat, lon, BuildConfig.OPENWEATHER_API_KEY)
                val forecastResponse = api.getForecastData(lat, lon, BuildConfig.OPENWEATHER_API_KEY)

                var cityName = weatherResponse.name
                var countryName = weatherResponse.sys.country
                try {
                    val googleResponse = googleApi.getAddressFromCoordinates("${lat},${lon}", BuildConfig.GOOGLE_MAPS_API_KEY)
                    if (googleResponse.status == "OK" && googleResponse.results.isNotEmpty()) {
                        val addressComponents = googleResponse.results.first().addressComponents
                        val cityComponent = addressComponents.find {
                            it.types.contains("locality") || it.types.contains("administrative_area_level_2")
                        }
                        cityName = cityComponent?.longName ?: weatherResponse.name
                        
                        val countryComponent = addressComponents.find { it.types.contains("country") }
                        countryName = countryComponent?.longName ?: weatherResponse.sys.country
                    }
                } catch (e: Exception) { e.printStackTrace() }

                val weatherInfo = weatherResponse.toWeatherInfo(forecastResponse).copy(
                    cityName = cityName,
                    country = countryName
                )

                dao.insertWeather(
                    WeatherEntity(
                        cityId = cityId,
                        lastUpdated = System.currentTimeMillis(),
                        weatherDataJson = gson.toJson(weatherInfo)
                    )
                )
                
                emit(Resource.Success(weatherInfo))
            } catch (e: Exception) {
                e.printStackTrace()
                val anyCache = dao.getLatestWeatherSync()
                if (anyCache == null) {
                    emit(Resource.Error("Check your internet connection"))
                }
            }
        }
        
        emit(Resource.Loading(false))
    }

    override fun getWeatherByCity(cityName: String): Flow<Resource<WeatherInfo>> = flow {
        emit(Resource.Loading(true))
        try {
            val googleResponse = googleApi.getCoordinatesFromAddress(cityName, BuildConfig.GOOGLE_MAPS_API_KEY)
            
            if (googleResponse.status == "OK" && googleResponse.results.isNotEmpty()) {
                val result = googleResponse.results.first()
                val lat = result.geometry.location.lat
                val lon = result.geometry.location.lng
                val actualCityName = result.addressComponents.find { 
                    it.types.contains("locality") || it.types.contains("administrative_area_level_2")
                }?.longName ?: result.formattedAddress
                
                val actualCountryName = result.addressComponents.find { 
                    it.types.contains("country")
                }?.longName ?: ""

                val weatherResponse = api.getWeatherData(lat, lon, BuildConfig.OPENWEATHER_API_KEY)
                val forecastResponse = api.getForecastData(lat, lon, BuildConfig.OPENWEATHER_API_KEY)
                val weatherInfo = weatherResponse.toWeatherInfo(forecastResponse).copy(
                    cityName = actualCityName,
                    country = actualCountryName
                )
                
                val roundedLat = String.format(Locale.US, Constants.ROUNDED_COORDINATE_FORMAT, lat).toDouble()
                val roundedLon = String.format(Locale.US, Constants.ROUNDED_COORDINATE_FORMAT, lon).toDouble()
                val cityId = "${roundedLat},${roundedLon}"

                dao.insertWeather(
                    WeatherEntity(
                        cityId = cityId,
                        lastUpdated = System.currentTimeMillis(),
                        weatherDataJson = gson.toJson(weatherInfo)
                    )
                )
                
                emit(Resource.Success(weatherInfo))
            } else {
                emit(Resource.Error("City not found"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error("Network error: ${e.message}"))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getLatestCachedWeather(): WeatherInfo? {
        return dao.getLatestWeatherSync()?.let { entity ->
            gson.fromJson(entity.weatherDataJson, WeatherInfo::class.java)
        }
    }
}
