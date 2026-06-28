package com.vibhanshu.wrbtechassessment

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.vibhanshu.wrbtechassessment.presentation.weather.WeatherThemeHelper
import com.vibhanshu.wrbtechassessment.presentation.weather.WeatherViewModel
import com.vibhanshu.wrbtechassessment.presentation.weather.components.DailyForecastCard
import com.vibhanshu.wrbtechassessment.presentation.weather.components.HourlyForecastCard
import com.vibhanshu.wrbtechassessment.presentation.weather.components.WeatherDetailsGrid
import com.vibhanshu.wrbtechassessment.presentation.weather.components.WeatherHeader
import com.vibhanshu.wrbtechassessment.ui.theme.WrbTechAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WrbTechAssessmentTheme {
                val state by viewModel.state.collectAsState()
                val weatherColors = WeatherThemeHelper.getColors(state.weatherInfo)
                
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                        viewModel.loadWeatherInfo()
                    }
                }

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }

                val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }
                
                LaunchedEffect(state.error) {
                    state.error?.let {
                        snackbarHostState.showSnackbar(it)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(weatherColors.gradientStart, weatherColors.gradientEnd)
                            )
                        )
                    ) {
                        val backgroundUrl = (weatherColors.backgroundImageUrl ?: "").toString()
                        if (backgroundUrl.isNotEmpty()) {
                            AsyncImage(
                                model = backgroundUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                alpha = 0.5f
                            )
                        }

                        PullToRefreshBox(
                            isRefreshing = state.isLoading,
                            onRefresh = { viewModel.loadWeatherInfo() },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            state.weatherInfo?.let {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    WeatherHeader(state = state)

                                    DailyForecastCard(
                                        state = state,
                                        backgroundColor = weatherColors.cardBackground,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    HourlyForecastCard(
                                        state = state,
                                        backgroundColor = weatherColors.cardBackground,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    WeatherDetailsGrid(
                                        state = state,
                                        cardColor = weatherColors.cardBackground,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                        
                        state.error?.let { error ->
                            if (state.weatherInfo == null) {
                                Text(
                                    text = error,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
