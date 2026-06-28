package com.vibhanshu.wrbtechassessment.data.remote

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("list")
    val list: List<ForecastItemDto>,
    @SerializedName("city")
    val city: CityDto
)

data class ForecastItemDto(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("main")
    val main: MainDto,
    @SerializedName("weather")
    val weather: List<WeatherDescriptionDto>,
    @SerializedName("wind")
    val wind: WindDto,
    @SerializedName("dt_txt")
    val dtTxt: String
)

data class CityDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("coord")
    val coord: CoordDto,
    @SerializedName("country")
    val country: String,
    @SerializedName("timezone")
    val timezone: Int
)
