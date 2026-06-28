package com.vibhanshu.wrbtechassessment.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey val cityId: String, // e.g., "lat,lon"
    val lastUpdated: Long,
    val weatherDataJson: String // Serialized WeatherDto or domain model
)
