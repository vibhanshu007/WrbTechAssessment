package com.vibhanshu.wrbtechassessment.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vibhanshu.wrbtechassessment.MainActivity
import com.vibhanshu.wrbtechassessment.R
import com.vibhanshu.wrbtechassessment.core.util.Constants
import com.vibhanshu.wrbtechassessment.core.util.Resource
import com.vibhanshu.wrbtechassessment.domain.location.LocationTracker
import com.vibhanshu.wrbtechassessment.domain.model.WeatherType
import com.vibhanshu.wrbtechassessment.domain.usecase.GetWeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationTracker: LocationTracker
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // STEP 1: Immediate notification to prove the worker started
        showWeatherAlert("Background Sync Started")

        return try {
            val location = locationTracker.getCurrentLocation() 
            if (location == null) {
                showWeatherAlert("Sync Error: Location not found")
                return Result.retry()
            }
            
            // Collect the first result (success or error) from the flow
            val result = getWeatherUseCase(location.latitude, location.longitude, true)
                .first { it !is Resource.Loading }

            if (result is Resource.Success) {
                val weather = result.data?.currentWeatherData
                
                showWeatherAlert("Sync Complete (Testing)")

                // Advanced requirement: Alert on extreme weather
                if (weather?.weatherType is WeatherType.HeavyRain || 
                    weather?.weatherType is WeatherType.HeavyThunderstormWithHail ||
                    weather?.weatherType is WeatherType.ModerateThunderstorm) {
                    showWeatherAlert("Extreme Weather: " + weather.weatherType.description)
                }
                Result.success()
            } else {
                showWeatherAlert("Sync Error: Network failed")
                Result.retry()
            }
        } catch (e: Exception) {
            showWeatherAlert("Sync Fatal Error: ${e.message}")
            Result.failure()
        }
    }

    private fun showWeatherAlert(description: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = Constants.NOTIFICATION_CHANNEL_ID

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Severe Weather Alert")
            .setContentText("Warning: $description detected in your area.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(Constants.NOTIFICATION_ID_WEATHER, notification)
    }
}
