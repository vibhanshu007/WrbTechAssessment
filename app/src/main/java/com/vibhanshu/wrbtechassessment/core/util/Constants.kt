package com.vibhanshu.wrbtechassessment.core.util

object Constants {
    const val WEATHER_DATABASE_NAME = "weather.db"
    const val WEATHER_IMAGE_CACHE_DIR = "weather_image_cache"
    
    const val ROUNDED_COORDINATE_FORMAT = "%.2f"
    const val DATE_TIME_FORMAT_DAILY = "MMM dd, HH:mm"
    const val DATE_TIME_FORMAT_HOURLY = "HH:mm"
    const val TIME_FORMAT_SUN = "HH:mm"

    const val CACHE_TTL_MINUTES = 30
    const val CACHE_TTL_MILLIS = CACHE_TTL_MINUTES * 60 * 1000L

    const val NOTIFICATION_CHANNEL_ID = "weather_alerts"
    const val NOTIFICATION_CHANNEL_NAME = "Weather Alerts"
    const val NOTIFICATION_ID_WEATHER = 1
}
