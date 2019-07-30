package com.github.jefferson.maps.common.repositories

import android.content.Context
import com.github.jefferson.maps.R
import com.github.jefferson.maps.common.ktx.injectApi
import com.github.jefferson.maps.common.ktx.requestConfig
import org.koin.core.KoinComponent
import org.koin.core.get

class PlacesRepository : KoinComponent {

    private val placesApi: PlacesApi by injectApi()

    fun getRoute() = placesApi.getRoute(
        key = get<Context>().getString(R.string.google_places_api_key)
    ).requestConfig()

}