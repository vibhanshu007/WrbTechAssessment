package com.vibhanshu.wrbtechassessment.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.presentation.weather.WeatherState
import kotlin.math.roundToInt

data class DefaultCity(val name: String, val lat: Double, val lon: Double, val country: String)

val defaultCities = listOf(
    DefaultCity("Sydney", -33.862250, 151.207684, "Australia"),
    DefaultCity("Tokyo", 35.676423, 139.650027, "Japan"),
    DefaultCity("Ho Chi Minh City", 10.823099, 106.629664, "Vietnam"),
    DefaultCity("Bengaluru", 12.962896, 77.577540, "India"),
    DefaultCity("Delhi", 28.704059, 77.102490, "India"),
    DefaultCity("Dubai", 25.204849, 55.270783, "UAE"),
    DefaultCity("London", 51.507218, -0.127586, "UK"),
    DefaultCity("New York", 40.712775, -74.005973, "USA")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: WeatherState,
    onCitySelected: (Double, Double, String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.manage_cities), 
                        fontSize = 24.sp, 
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionHeader(stringResource(R.string.current_location))
                state.weatherInfo?.let { current ->
                    CityCard(
                        name = current.cityName,
                        temp = current.currentWeatherData?.temperatureCelsius?.roundToInt() ?: 0,
                        description = current.currentWeatherData?.weatherType?.description ?: "",
                        maxTemp = current.currentWeatherData?.tempMax?.roundToInt() ?: 0,
                        minTemp = current.currentWeatherData?.tempMin?.roundToInt() ?: 0,
                        isCurrentLocation = true,
                        onClick = { onBack() }
                    )
                }
            }

            if (defaultCities.isNotEmpty()) {
                item {
                    SectionHeader(stringResource(R.string.default_cities))
                }
                items(defaultCities) { city ->
                    CityCard(
                        name = city.name,
                        country = city.country,
                        onClick = {
                            onCitySelected(city.lat, city.lon, city.name)
                        }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun CityCard(
    name: String,
    temp: Int? = null,
    description: String = "",
    maxTemp: Int? = null,
    minTemp: Int? = null,
    country: String? = null,
    isCurrentLocation: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        color = Color(0xFF1C1C1E),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (isCurrentLocation) {
                        Spacer(modifier = Modifier.size(4.dp))
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = description.ifEmpty { country ?: "" },
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            if (temp != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$temp°",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light
                    )
                    if (maxTemp != null && minTemp != null) {
                        Text(
                            text = "$maxTemp° / $minTemp°",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
