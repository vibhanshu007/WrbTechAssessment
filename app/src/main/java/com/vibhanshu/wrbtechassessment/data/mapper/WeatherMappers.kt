package com.vibhanshu.wrbtechassessment.data.mapper

import com.vibhanshu.wrbtechassessment.data.remote.ForecastDto
import com.vibhanshu.wrbtechassessment.data.remote.WeatherDto
import com.vibhanshu.wrbtechassessment.domain.model.WeatherData
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.model.WeatherType

fun WeatherDto.toWeatherInfo(forecastDto: ForecastDto? = null): WeatherInfo {
    return WeatherInfo(
        currentWeatherData = WeatherData(
            time = dt * 1000,
            temperatureCelsius = main.temp,
            pressure = main.pressure.toDouble(),
            windSpeed = wind.speed,
            humidity = main.humidity.toDouble(),
            weatherType = WeatherType.fromOpenWeatherId(weather.firstOrNull()?.id ?: 0),
            feelsLike = main.feelsLike,
            tempMin = main.tempMin,
            tempMax = main.tempMax
        ),
        forecastData = forecastDto?.list?.map { item ->
            WeatherData(
                time = item.dt * 1000,
                temperatureCelsius = item.main.temp,
                pressure = item.main.pressure.toDouble(),
                windSpeed = item.wind.speed,
                humidity = item.main.humidity.toDouble(),
                weatherType = WeatherType.fromOpenWeatherId(item.weather.firstOrNull()?.id ?: 0),
                feelsLike = item.main.feelsLike,
                tempMin = item.main.tempMin,
                tempMax = item.main.tempMax
            )
        } ?: emptyList(),
        cityName = name,
        country = sys.country,
        sunrise = sys.sunrise * 1000,
        sunset = sys.sunset * 1000
    )
}

// Mapping OpenWeather condition codes to our WeatherType
// Reference: https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
fun WeatherType.Companion.fromOpenWeatherId(code: Int): WeatherType {
    return when(code) {
        in 200..202 -> WeatherType.HeavyThunderstormWithHail
        in 210..232 -> WeatherType.ModerateThunderstorm
        in 300..321 -> WeatherType.LightDrizzle
        in 500..504 -> WeatherType.ModerateRain
        511 -> WeatherType.LightFreezingRain
        in 520..531 -> WeatherType.SlightRainShowers
        in 600..602 -> WeatherType.ModerateSnowFall
        in 611..616 -> WeatherType.SlightSnowShowers
        in 620..622 -> WeatherType.HeavySnowShowers
        in 701..781 -> WeatherType.Foggy
        800 -> WeatherType.ClearSky
        801 -> WeatherType.MainlyClear
        802 -> WeatherType.PartlyCloudy
        in 803..804 -> WeatherType.Overcast
        else -> WeatherType.ClearSky
    }
}
