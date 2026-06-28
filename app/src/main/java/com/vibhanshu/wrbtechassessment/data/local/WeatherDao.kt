package com.vibhanshu.wrbtechassessment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table WHERE cityId = :cityId")
    suspend fun getWeatherByCitySync(cityId: String): WeatherEntity?

    @Query("SELECT * FROM weather_table ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestWeatherSync(): WeatherEntity?
}
