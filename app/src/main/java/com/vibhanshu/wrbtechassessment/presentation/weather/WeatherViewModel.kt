package com.vibhanshu.wrbtechassessment.presentation.weather

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.domain.location.LocationTracker
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationTracker: LocationTracker,
    private val app: Application,
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getWeatherUseCase.getLatestCachedWeather()?.let { info ->
                _state.update { it.copy(weatherInfo = info) }
            }
        }
    }

    fun searchCity(cityName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getWeatherUseCase.getWeatherByCity(cityName).collect { result ->
                handleResult(result)
            }
        }
    }

    fun loadWeatherWithCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getWeatherUseCase(lat = lat, lon = lon, fetchFromRemote = true).collect { result ->
                handleResult(result)
            }
        }
    }

    fun loadWeatherInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                getWeatherUseCase(lat = location.latitude, lon = location.longitude, fetchFromRemote = true).collect { result ->
                    handleResult(result)
                }
            } else {
                _state.update { it.copy(
                    isLoading = false,
                    error = app.getString(R.string.error_location_disabled)
                ) }
            }
        }
    }

    private fun handleResult(result: Resource<WeatherInfo>) {
        when (result) {
            is Resource.Success -> {
                _state.update { it.copy(
                    weatherInfo = result.data,
                    isLoading = false,
                    error = null
                ) }
            }
            is Resource.Error -> {
                _state.update { it.copy(
                    isLoading = false,
                    error = result.message ?: app.getString(R.string.error_no_internet)
                ) }
            }
            is Resource.Loading -> {
                _state.update { it.copy(isLoading = result.isLoading) }
            }
        }
    }
}
