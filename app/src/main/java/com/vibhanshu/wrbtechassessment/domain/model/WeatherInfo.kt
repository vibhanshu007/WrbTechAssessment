package com.vibhanshu.wrbtechassessment.domain.model

data class WeatherInfo(
    val currentWeatherData: WeatherData?,
    val forecastData: List<WeatherData>,
    val cityName: String,
    val country: String = "",
    val sunrise: Long = 0,
    val sunset: Long = 0
)

data class WeatherData(
    val time: Long,
    val temperatureCelsius: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val weatherType: WeatherType,
    val feelsLike: Double = 0.0,
    val tempMin: Double = 0.0,
    val tempMax: Double = 0.0,
    val uvIndex: Double = 0.0 // Added for the UI requirements
)

sealed class WeatherType(
    val description: String,
    val iconRes: Int 
) {
    data object ClearSky : WeatherType("Clear sky", 0) 
    data object MainlyClear : WeatherType("Mainly clear", 0)
    data object PartlyCloudy : WeatherType("Partly cloudy", 0)
    data object Overcast : WeatherType("Overcast", 0)
    data object Foggy : WeatherType("Foggy", 0)
    data object DepositingRimeFog : WeatherType("Depositing rime fog", 0)
    data object LightDrizzle : WeatherType("Light drizzle", 0)
    data object ModerateDrizzle : WeatherType("Moderate drizzle", 0)
    data object DenseDrizzle : WeatherType("Dense drizzle", 0)
    data object LightFreezingDrizzle : WeatherType("Light freezing drizzle", 0)
    data object DenseFreezingDrizzle : WeatherType("Dense freezing drizzle", 0)
    data object SlightRain : WeatherType("Slight rain", 0)
    data object ModerateRain : WeatherType("Moderate rain", 0)
    data object HeavyRain : WeatherType("Heavy rain", 0)
    data object HeavyFreezingRain : WeatherType("Heavy freezing rain", 0)
    data object LightFreezingRain : WeatherType("Light freezing rain", 0)
    data object SlightSnowFall : WeatherType("Slight snow fall", 0)
    data object ModerateSnowFall : WeatherType("Moderate snow fall", 0)
    data object HeavySnowFall : WeatherType("Heavy snow fall", 0)
    data object SnowGrains : WeatherType("Snow grains", 0)
    data object SlightRainShowers : WeatherType("Slight rain showers", 0)
    data object ModerateRainShowers : WeatherType("Moderate rain showers", 0)
    data object ViolentRainShowers : WeatherType("Violent rain showers", 0)
    data object SlightSnowShowers : WeatherType("Slight snow showers", 0)
    data object HeavySnowShowers : WeatherType("Heavy snow showers", 0)
    data object ModerateThunderstorm : WeatherType("Moderate thunderstorm", 0)
    data object SlightThunderstormWithHail : WeatherType("Slight thunderstorm with hail", 0)
    data object HeavyThunderstormWithHail : WeatherType("Heavy thunderstorm with hail", 0)

    companion object
}
