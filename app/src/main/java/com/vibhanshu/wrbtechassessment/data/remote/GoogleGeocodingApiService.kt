package com.vibhanshu.wrbtechassessment.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleGeocodingApiService {
    @GET("maps/api/geocode/json")
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GoogleGeocodingResponse

    @GET("maps/api/geocode/json")
    suspend fun getCoordinatesFromAddress(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GoogleGeocodingResponse

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}
