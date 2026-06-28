package com.vibhanshu.wrbtechassessment.presentation.weather

import androidx.compose.ui.graphics.Color
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.model.WeatherType
import java.util.Calendar

data class WeatherColors(
    val gradientStart: Color,
    val gradientEnd: Color,
    val cardBackground: Color = Color.White.copy(alpha = 0.12f),
    val textColor: Color = Color.White
)

object WeatherThemeHelper {
    fun getColors(info: WeatherInfo?): WeatherColors {
        val current = info?.currentWeatherData
        
        val isDay = if (info != null && current != null && info.sunrise != 0L && info.sunset != 0L) {
            current.time in (info.sunrise..info.sunset)
        } else {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            hour in 6..18
        }
        
        val type = current?.weatherType
        
        return when {
            type is WeatherType.ClearSky || type is WeatherType.MainlyClear -> {
                if (isDay) clearDay else clearNight
            }
            
            type is WeatherType.PartlyCloudy || type is WeatherType.Overcast || 
            type is WeatherType.Foggy || type is WeatherType.DepositingRimeFog -> {
                if (isDay) cloudyDay else cloudyNight
            }
            
            type is WeatherType.SlightRain || type is WeatherType.ModerateRain || type is WeatherType.HeavyRain ||
            type is WeatherType.LightDrizzle || type is WeatherType.ModerateDrizzle || type is WeatherType.DenseDrizzle ||
            type is WeatherType.SlightRainShowers || type is WeatherType.ModerateRainShowers || 
            type is WeatherType.ViolentRainShowers || type is WeatherType.LightFreezingRain || type is WeatherType.HeavyFreezingRain -> {
                if (isDay) rainyDay else rainyNight
            }
            
            type is WeatherType.SlightSnowFall || type is WeatherType.ModerateSnowFall || type is WeatherType.HeavySnowFall ||
            type is WeatherType.SnowGrains || type is WeatherType.SlightSnowShowers || type is WeatherType.HeavySnowShowers -> {
                if (isDay) snowyDay else snowyNight
            }
            
            type is WeatherType.ModerateThunderstorm || type is WeatherType.SlightThunderstormWithHail || type is WeatherType.HeavyThunderstormWithHail -> {
                stormy
            }
            
            else -> if (isDay) clearDay else clearNight
        }
    }

    private val clearDay = WeatherColors(
        gradientStart = Color(0xFF29B6F6), 
        gradientEnd = Color(0xFF0288D1)
    )

    private val clearNight = WeatherColors(
        gradientStart = Color(0xFF1A237E), 
        gradientEnd = Color(0xFF010203)
    )

    private val cloudyDay = WeatherColors(
        gradientStart = Color(0xFF4FC3F7), 
        gradientEnd = Color(0xFF5C6BC0)
    )

    private val cloudyNight = WeatherColors(
        gradientStart = Color(0xFF303F9F), 
        gradientEnd = Color(0xFF000000)
    )

    private val rainyDay = WeatherColors(
        gradientStart = Color(0xFF607D8B), 
        gradientEnd = Color(0xFF455A64)
    )

    private val rainyNight = WeatherColors(
        gradientStart = Color(0xFF263238), 
        gradientEnd = Color(0xFF000000)
    )

    private val snowyDay = WeatherColors(
        gradientStart = Color(0xFFE1F5FE), 
        gradientEnd = Color(0xFFB3E5FC)
    )

    private val snowyNight = WeatherColors(
        gradientStart = Color(0xFF455A64), 
        gradientEnd = Color(0xFF102027)
    )

    private val stormy = WeatherColors(
        gradientStart = Color(0xFF212121), 
        gradientEnd = Color(0xFF000000)
    )
}
