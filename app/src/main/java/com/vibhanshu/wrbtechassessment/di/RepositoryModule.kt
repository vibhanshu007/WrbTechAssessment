package com.vibhanshu.wrbtechassessment.di

import com.vibhanshu.wrbtechassessment.data.location.DefaultLocationTracker
import com.vibhanshu.wrbtechassessment.data.repository.WeatherRepositoryImpl
import com.vibhanshu.wrbtechassessment.domain.location.LocationTracker
import com.vibhanshu.wrbtechassessment.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        defaultLocationTracker: DefaultLocationTracker
    ): LocationTracker
}
