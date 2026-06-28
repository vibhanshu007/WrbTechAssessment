package com.vibhanshu.wrbtechassessment.presentation.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.domain.model.WeatherType
import com.vibhanshu.wrbtechassessment.presentation.weather.WeatherState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHeader(
    state: WeatherState,
    onSearchCity: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    state.weatherInfo?.let { info ->
        val current = info.currentWeatherData ?: return@let
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = info.cityName.ifBlank { stringResource(R.string.unknown_location) },
                        fontSize = 24.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = textColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = info.country,
                            fontSize = 12.sp,
                            color = textColor,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(textColor, CircleShape)
                        )
                    }
                }
                Row {
                    IconButton(onClick = { onSearchCity() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_city),
                            tint = textColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${current.temperatureCelsius.roundToInt()}°",
                        fontSize = 100.sp,
                        color = textColor,
                        fontWeight = FontWeight.Light,
                        lineHeight = 100.sp
                    )

                    Text(
                        text = "${current.weatherType.description}  ${current.tempMax.roundToInt()}°/${current.tempMin.roundToInt()}°",
                        fontSize = 18.sp,
                        color = textColor,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Weather Emoji Mapping
                val emoji = when(current.weatherType) {
                    is WeatherType.ClearSky -> "☀️"
                    is WeatherType.MainlyClear -> "🌤️"
                    is WeatherType.PartlyCloudy -> "⛅"
                    is WeatherType.Overcast -> "☁️"
                    is WeatherType.Foggy, is WeatherType.DepositingRimeFog -> "🌫️"
                    is WeatherType.LightDrizzle, is WeatherType.ModerateDrizzle, is WeatherType.DenseDrizzle -> "🌦️"
                    is WeatherType.SlightRain, is WeatherType.ModerateRain, is WeatherType.HeavyRain -> "🌧️"
                    is WeatherType.ModerateThunderstorm, is WeatherType.SlightThunderstormWithHail, is WeatherType.HeavyThunderstormWithHail -> "⛈️"
                    else -> "☀️"
                }

                Text(
                    text = emoji,
                    fontSize = 80.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = textColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val aqi = info.aqi
                    Text(
                        text = stringResource(R.string.aqi_label, aqi.roundToInt()),
                        fontSize = 14.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
