package com.vibhanshu.wrbtechassessment.data.remote

import com.google.gson.annotations.SerializedName

data class AirPollutionDto(
    @SerializedName("list")
    val list: List<AirPollutionItemDto>
)

data class AirPollutionItemDto(
    @SerializedName("main")
    val main: AirPollutionMainDto,
    @SerializedName("components")
    val components: AirPollutionComponentsDto,
    @SerializedName("dt")
    val dt: Long
)

data class AirPollutionMainDto(
    @SerializedName("aqi")
    val aqi: Int
)

data class AirPollutionComponentsDto(
    @SerializedName("o3")
    val o3: Double
)
