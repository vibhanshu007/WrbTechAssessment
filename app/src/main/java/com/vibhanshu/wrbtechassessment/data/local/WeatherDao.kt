package com.vibhanshu.wrbtechassessment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table WHERE cityId = :cityId")
    fun getWeatherByCity(cityId: String): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather_table WHERE cityId = :cityId")
    suspend fun getWeatherByCitySync(cityId: String): WeatherEntity?

    @Query("SELECT * FROM weather_table ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestWeatherSync(): WeatherEntity?

    @Query("DELETE FROM weather_table")
    suspend fun clearWeather()
}
