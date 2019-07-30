package com.github.jefferson.maps.common.repositories

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.SettingsClient
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

class LocationRepository : KoinComponent {

    val locationProvider: FusedLocationProviderClient by inject()

    fun checkGpsSettings(): Completable = Completable.create { emitter ->
        get<SettingsClient>().checkLocationSettings(get()).addOnSuccessListener {
            emitter.onComplete()
        }.addOnFailureListener { exception ->
            emitter.onError(exception)
        }
    }

    fun getLocationUpdates(): Observable<Location> = checkGpsSettings().andThen(Observable.create {
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(get(), object : LocationCallback() {

                override fun onLocationResult(locationResult: LocationResult?) {
                    if (it.isDisposed) {
                        locationProvider.removeLocationUpdates(this)
                    } else {
                        val location = locationResult?.lastLocation ?: return
                        it.onNext(location)
                    }
                }
            }, null)
        } else {
            it.onError(Exception("Location permission denied"))
        }
    })

    fun getCurrentLocation(): Single<Location> = checkGpsSettings().andThen(Single.create {
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(get(), object : LocationCallback() {

                override fun onLocationResult(locationResult: LocationResult?) {
                    locationProvider.removeLocationUpdates(this)
                    val location = locationResult?.lastLocation
                    if (!it.isDisposed && location != null) {
                        it.onSuccess(location)
                    } else {
                        it.tryOnError(Exception("Current location not found"))
                    }
                }
            }, null)
        } else {
            it.onError(Exception("Location permission denied"))
        }
    })

    fun getLastLocation(): Single<Location> = Single.create {
        if (ActivityCompat.checkSelfPermission(get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    it.onSuccess(location)
                } else {
                    it.onError(Exception("Last location not found"))
                }
            }.addOnFailureListener { exception ->
                it.tryOnError(exception)
            }
        } else {
            it.onError(Exception("Location permission denied"))
        }
    }
}