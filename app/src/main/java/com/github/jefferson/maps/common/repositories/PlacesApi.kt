package com.github.jefferson.maps.common.repositories

import android.content.Context
import com.github.jefferson.maps.R
import com.google.gson.JsonObject
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("maps/api/directions/json")
    fun getRoute(
        @Query("origin") origin: String = "4.667426,-74.056624",
        @Query("destination") destination: String = "4.672655,-74.054071",
        @Query("mode") mode: String = "driving",
        @Query("key") key: String
    ): Single<JsonObject>
}