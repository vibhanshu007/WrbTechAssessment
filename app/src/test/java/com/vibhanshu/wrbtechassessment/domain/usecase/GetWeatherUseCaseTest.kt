package com.vibhanshu.wrbtechassessment.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.domain.model.WeatherInfo
import com.vibhanshu.wrbtechassessment.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetWeatherUseCaseTest {

    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private val repository: WeatherRepository = mockk()

    @Before
    fun setUp() {
        getWeatherUseCase = GetWeatherUseCase(repository)
    }

    @Test
    fun `invoke should return data from repository`() = runBlocking {
        // Given
        val weatherInfo = WeatherInfo(
            currentWeatherData = null,
            forecastData = emptyList(),
            cityName = "Test City",
            country = "Test Country"
        )
        val flow = flowOf(Resource.Success(weatherInfo))
        every { repository.getWeatherData(any(), any(), any()) } returns flow

        // When
        val result = getWeatherUseCase(1.0, 1.0, true)

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission).isInstanceOf(Resource.Success::class.java)
            assertThat(emission.data?.cityName).isEqualTo("Test City")
            awaitComplete()
        }
    }

    @Test
    fun `getWeatherByCity should return data from repository`() = runBlocking {
        // Given
        val weatherInfo = WeatherInfo(
            currentWeatherData = null,
            forecastData = emptyList(),
            cityName = "Test City",
            country = "Test Country"
        )
        val flow = flowOf(Resource.Success(weatherInfo))
        every { repository.getWeatherByCity(any()) } returns flow

        // When
        val result = getWeatherUseCase.getWeatherByCity("Test City")

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission).isInstanceOf(Resource.Success::class.java)
            assertThat(emission.data?.cityName).isEqualTo("Test City")
            awaitComplete()
        }
    }
}
