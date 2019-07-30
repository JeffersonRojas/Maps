package com.github.jefferson.maps.route.viewmodels

import android.location.Location
import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.github.jefferson.maps.common.ktx.metterPerSecondToKh
import com.github.jefferson.maps.common.ktx.toLatLong
import com.github.jefferson.maps.common.ktx.toMarker
import com.github.jefferson.maps.common.repositories.LocationRepository
import com.github.jefferson.maps.common.viewmodels.BaseViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.uber.autodispose.lifecycle.autoDisposable
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.inject

class RouteViewModel : BaseViewModel() {

    private val locationRepository: LocationRepository by inject()

    lateinit var listener: Listener

    lateinit var googleMap: GoogleMap

    val speed = ObservableInt(50)

    fun onMapReady(map: GoogleMap) {
        googleMap = map
        startLocationUpdates()
    }

    fun startLocationUpdates() {
        listener.checkGpsPermissions()
            .andThen(locationRepository.checkGpsSettings())
            .andThen(locationRepository.getLocationUpdates())
            .autoDisposable(this)
            .subscribe(this::onLocationFound) {
                when (it) {
                    is ResolvableApiException -> listener.tryToResolveException(it)
                    else -> {
                        // TODO - show a ui dialog explain to user
                        startLocationUpdates()
                    }
                }
            }
    }

    private fun onLocationFound(location: Location) {
        speed.set(location.speed.metterPerSecondToKh().toInt())
        googleMap.addMarker(location.toMarker())
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.toLatLong(), 15f))
    }

    interface Listener {

        fun checkGpsPermissions(): Completable

        fun tryToResolveException(exception: ResolvableApiException)

    }
}