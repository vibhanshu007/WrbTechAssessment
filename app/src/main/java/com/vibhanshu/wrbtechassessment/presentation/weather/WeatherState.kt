package com.vibhanshu.wrbtechassessment.presentation.weather

import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val searchHistory: List<WeatherInfo> = emptyList()
)
