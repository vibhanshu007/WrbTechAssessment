package com.vibhanshu.wrbtechassessment.presentation.weather.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.core.util.Constants
import com.vibhanshu.wrbtechassessment.presentation.weather.WeatherState
import kotlin.math.roundToInt

@Composable
fun WeatherDetailsGrid(
    state: WeatherState,
    cardColor: Color,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.currentWeatherData?.let { data ->
        Column(modifier = modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                DetailCard(
                    title = stringResource(R.string.uv_index),
                    value = stringResource(R.string.uv_weak),
                    icon = Icons.Default.WbSunny,
                    cardColor = cardColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Gauge(progress = 0.2f, color = Color.Green)
                }
                Spacer(modifier = Modifier.width(16.dp))
                DetailCard(
                    title = stringResource(R.string.humidity),
                    value = "${data.humidity.roundToInt()}%",
                    icon = Icons.Default.WaterDrop,
                    cardColor = cardColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Gauge(progress = (data.humidity / 100f).toFloat(), color = Color.Cyan)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                DetailCard(
                    title = stringResource(R.string.real_feel),
                    value = "${data.feelsLike.roundToInt()}°",
                    icon = Icons.Default.Thermostat,
                    cardColor = cardColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Gauge(progress = 0.5f, color = Color.Yellow)
                }
                Spacer(modifier = Modifier.width(16.dp))
                DetailCard(
                    title = stringResource(R.string.wind),
                    value = "${data.windSpeed.roundToInt()} ${stringResource(R.string.unit_km_h)}",
                    icon = Icons.Default.Air,
                    cardColor = cardColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Gauge(progress = 0.3f, color = Color.Blue)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                val isDay = data.time in state.weatherInfo.sunrise..state.weatherInfo.sunset
                val sunTime = if (isDay) state.weatherInfo.sunset else state.weatherInfo.sunrise
                val sunLabel = if (isDay) stringResource(R.string.sunset) else stringResource(R.string.sunrise)
                
                DetailCard(
                    title = sunLabel,
                    value = java.time.Instant.ofEpochMilli(sunTime)
                        .atZone(java.time.ZoneId.systemDefault())
                        .format(java.time.format.DateTimeFormatter.ofPattern(Constants.TIME_FORMAT_SUN)),
                    icon = Icons.Default.WbTwilight,
                    cardColor = cardColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                        drawArc(
                            color = Color.Yellow.copy(alpha = 0.3f),
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = false,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )
                        
                        if (state.weatherInfo.sunrise != 0L && state.weatherInfo.sunset != 0L) {
                            val totalDaylight = state.weatherInfo.sunset - state.weatherInfo.sunrise
                            val progress = if (isDay) {
                                (data.time - state.weatherInfo.sunrise).toFloat() / totalDaylight
                            } else 0f
                            
                            if (isDay) {
                                val angle = 180f + (180f * progress)
                                val radius = size.width / 2
                                val x = center.x + radius * kotlin.math.cos(angle * kotlin.math.PI / 180f).toFloat()
                                val y = center.y + radius * kotlin.math.sin(angle * kotlin.math.PI / 180f).toFloat()
                                drawCircle(Color.Yellow, radius = 4.dp.toPx(), center = androidx.compose.ui.geometry.Offset(x, y))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                DetailCard(
                    title = stringResource(R.string.pressure),
                    value = "${data.pressure.roundToInt()} ${stringResource(R.string.unit_hpa)}",
                    icon = Icons.Default.Speed,
                    cardColor = cardColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Gauge(progress = 0.5f, color = Color.Magenta)
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    value: String,
    icon: ImageVector,
    cardColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.height(160.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Text(
                text = value,
                fontSize = 20.sp,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun Gauge(progress: Float, color: Color) {
    Canvas(modifier = Modifier.size(50.dp)) {
        drawArc(
            color = Color.White.copy(alpha = 0.1f),
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = color,
            startAngle = 135f,
            sweepAngle = 270f * progress,
            useCenter = false,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
