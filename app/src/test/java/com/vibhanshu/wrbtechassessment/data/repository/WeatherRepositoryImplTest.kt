package com.vibhanshu.wrbtechassessment.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.data.local.WeatherDao
import com.vibhanshu.wrbtechassessment.data.local.WeatherDatabase
import com.vibhanshu.wrbtechassessment.data.local.WeatherEntity
import com.vibhanshu.wrbtechassessment.data.remote.GoogleGeocodingApiService
import com.vibhanshu.wrbtechassessment.data.remote.WeatherApiService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private lateinit var repository: WeatherRepositoryImpl
    private val api: WeatherApiService = mockk()
    private val googleApi: GoogleGeocodingApiService = mockk()
    private val db: WeatherDatabase = mockk()
    private val dao: WeatherDao = mockk()
    private val gson: Gson = Gson()

    @Before
    fun setUp() {
        every { db.dao } returns dao
        repository = WeatherRepositoryImpl(api, googleApi, db, gson)
    }

    @Test
    fun `getWeatherData should emit loading and cached data when available and fresh`() = runTest {
        val lat = 10.0
        val lon = 20.0
        val cityIdFormatted = "10.00,20.00"
        
        val cachedEntity = WeatherEntity(
            cityId = cityIdFormatted,
            lastUpdated = System.currentTimeMillis(), // Fresh data
            weatherDataJson = "{}" 
        )
        
        coEvery { dao.getWeatherByCitySync(any()) } returns cachedEntity
        coEvery { dao.getLatestWeatherSync() } returns cachedEntity
        
        repository.getWeatherData(lat, lon, fetchFromRemote = false).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Success::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            awaitComplete()
        }
    }
}
