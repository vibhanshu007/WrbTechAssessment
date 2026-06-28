package com.vibhanshu.wrbtechassessment.presentation.weather.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.domain.model.WeatherType
import com.vibhanshu.wrbtechassessment.presentation.weather.WeatherState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun DailyForecastCard(
    state: WeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.forecastData?.let { data ->
        val dailyData = data.groupBy { 
            Instant.ofEpochMilli(it.time).atZone(ZoneId.systemDefault()).toLocalDate() 
        }.toList().take(5)

        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(24.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.five_day_forecast),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                dailyData.forEach { (date, dayData) ->
                    val minTemp = dayData.minOf { it.temperatureCelsius }
                    val maxTemp = dayData.maxOf { it.temperatureCelsius }
                    val weatherType = dayData.first().weatherType
                    val dayName = if (date == java.time.LocalDate.now()) stringResource(R.string.today) 
                                 else if (date == java.time.LocalDate.now().plusDays(1)) stringResource(R.string.tomorrow)
                                 else date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase(Locale.getDefault()) }

                    DailyForecastItem(
                        day = dayName,
                        minTemp = minTemp.roundToInt(),
                        maxTemp = maxTemp.roundToInt(),
                        weatherType = weatherType
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.five_day_forecast), color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun DailyForecastItem(
    day: String,
    minTemp: Int,
    maxTemp: Int,
    weatherType: WeatherType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = day, color = Color.White, modifier = Modifier.weight(1f))
        
        val emoji = when(weatherType) {
            is WeatherType.ClearSky -> "☀️"
            is WeatherType.MainlyClear -> "🌤️"
            is WeatherType.PartlyCloudy -> "⛅"
            is WeatherType.Overcast -> "☁️"
            is WeatherType.Foggy, is WeatherType.DepositingRimeFog -> "🌫️"
            is WeatherType.LightDrizzle, is WeatherType.ModerateDrizzle, is WeatherType.DenseDrizzle -> "🌦️"
            is WeatherType.SlightRain, is WeatherType.ModerateRain, is WeatherType.HeavyRain -> "🌧️"
            is WeatherType.ModerateThunderstorm, is WeatherType.SlightThunderstormWithHail, is WeatherType.HeavyThunderstormWithHail -> "⛈️"
            else -> "☁️"
        }
        
        Text(text = emoji, fontSize = 20.sp, modifier = Modifier.weight(0.5f))
        
        Text(text = "$minTemp°", color = Color.White.copy(alpha = 0.7f))
        
        TemperatureBar(
            modifier = Modifier
                .width(80.dp)
                .padding(horizontal = 8.dp),
            currentMin = minTemp,
            currentMax = maxTemp,
            overallMin = 10,
            overallMax = 45 
        )
        
        Text(text = "$maxTemp°", color = Color.White)
    }
}

@Composable
fun TemperatureBar(
    modifier: Modifier = Modifier,
    currentMin: Int,
    currentMax: Int,
    overallMin: Int,
    overallMax: Int
) {
    Canvas(modifier = modifier.height(4.dp)) {
        val width = size.width
        val range = overallMax - overallMin
        val start = ((currentMin - overallMin).toFloat() / range) * width
        val end = ((currentMax - overallMin).toFloat() / range) * width
        
        drawRoundRect(
            color = Color.White.copy(alpha = 0.2f),
            size = size,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
        )
        
        drawRoundRect(
            brush = Brush.horizontalGradient(listOf(Color(0xFFFFA500), Color(0xFFFF4500))),
            topLeft = Offset(start, 0f),
            size = androidx.compose.ui.geometry.Size(end - start, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
        )
    }
}

@Composable
fun HourlyForecastCard(
    state: WeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.forecastData?.let { data ->
        val hourlyData = data.take(12)
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(24.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.twenty_four_hour_forecast),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(hourlyData) { weatherData ->
                        val time = Instant.ofEpochMilli(weatherData.time)
                            .atZone(ZoneId.systemDefault())
                        val formattedTime = if (time.toLocalDate() == java.time.LocalDate.now() && 
                                               time.hour == java.time.LocalTime.now().hour) stringResource(R.string.now)
                                            else time.format(DateTimeFormatter.ofPattern("HH:mm"))
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${weatherData.temperatureCelsius.roundToInt()}°",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            
                            val emoji = when(weatherData.weatherType) {
                                is WeatherType.ClearSky -> "☀️"
                                is WeatherType.MainlyClear -> "🌤️"
                                is WeatherType.PartlyCloudy -> "⛅"
                                is WeatherType.Overcast -> "☁️"
                                is WeatherType.SlightRain, is WeatherType.ModerateRain -> "🌧️"
                                is WeatherType.ModerateThunderstorm -> "⛈️"
                                else -> "☀️"
                            }
                            Text(text = emoji, fontSize = 20.sp)
                            
                            Text(
                                text = "${weatherData.windSpeed.roundToInt()} ${stringResource(R.string.unit_km_h)}",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            
                            Text(
                                text = formattedTime,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
