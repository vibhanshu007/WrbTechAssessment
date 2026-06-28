package com.vibhanshu.wrbtechassessment.data.remote

import com.google.gson.annotations.SerializedName

data class GoogleGeocodingResponse(
    @SerializedName("results")
    val results: List<GoogleGeocodingResult>,
    @SerializedName("status")
    val status: String
)

data class GoogleGeocodingResult(
    @SerializedName("formatted_address")
    val formattedAddress: String,
    @SerializedName("geometry")
    val geometry: GoogleGeometry,
    @SerializedName("address_components")
    val addressComponents: List<GoogleAddressComponent>
)

data class GoogleGeometry(
    @SerializedName("location")
    val location: GoogleLocation
)

data class GoogleLocation(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class GoogleAddressComponent(
    @SerializedName("long_name")
    val longName: String,
    @SerializedName("short_name")
    val shortName: String,
    @SerializedName("types")
    val types: List<String>
)
