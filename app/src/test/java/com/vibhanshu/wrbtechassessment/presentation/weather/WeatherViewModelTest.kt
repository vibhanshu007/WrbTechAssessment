package com.vibhanshu.wrbtechassessment.presentation.weather

import android.app.Application
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.domain.location.LocationTracker
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.usecase.GetWeatherUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private val getWeatherUseCase: GetWeatherUseCase = mockk()
    private val locationTracker: LocationTracker = mockk()
    private val app: Application = mockk()
    
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // Default mock responses for init
        coEvery { getWeatherUseCase.getLatestCachedWeather() } returns null
        
        viewModel = WeatherViewModel(
            getWeatherUseCase = getWeatherUseCase,
            locationTracker = locationTracker,
            app = app
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWeatherInfo should update state with weather info when location is available and use case returns success`() = runTest {
        val lat = 12.97
        val lon = 77.59
        val mockWeatherInfo = mockk<WeatherInfo>()
        
        val mockLocation = mockk<android.location.Location>()
        every { mockLocation.latitude } returns lat
        every { mockLocation.longitude } returns lon
        
        coEvery { locationTracker.getCurrentLocation() } returns mockLocation
        every { getWeatherUseCase(lat, lon, true) } returns flowOf(
            Resource.Loading(true),
            Resource.Success(mockWeatherInfo),
            Resource.Loading(false)
        )

        viewModel.loadWeatherInfo()
        advanceUntilIdle()
        
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.weatherInfo).isEqualTo(mockWeatherInfo)
            assertThat(state.isLoading).isFalse()
            assertThat(state.error).isNull()
        }
    }

    @Test
    fun `loadWeatherInfo should update state with error when location is disabled`() = runTest {
        val errorMsg = "Location disabled"
        coEvery { locationTracker.getCurrentLocation() } returns null
        every { app.getString(R.string.error_location_disabled) } returns errorMsg

        viewModel.loadWeatherInfo()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.error).isEqualTo(errorMsg)
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `searchCity should update state with weather info when successful`() = runTest {
        val cityName = "London"
        val mockWeatherInfo = mockk<WeatherInfo>()
        
        every { getWeatherUseCase.getWeatherByCity(cityName) } returns flowOf(
            Resource.Loading(true),
            Resource.Success(mockWeatherInfo),
            Resource.Loading(false)
        )

        viewModel.searchCity(cityName)
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.weatherInfo).isEqualTo(mockWeatherInfo)
            assertThat(state.isLoading).isFalse()
        }
    }
}
