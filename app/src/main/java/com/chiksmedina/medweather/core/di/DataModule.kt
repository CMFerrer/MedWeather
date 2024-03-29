package com.chiksmedina.medweather.core.di

import com.chiksmedina.medweather.search.data.SearchRepository
import com.chiksmedina.medweather.search.data.SearchRepositoryImpl
import com.chiksmedina.medweather.weather.data.WeatherRepository
import com.chiksmedina.medweather.weather.data.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}