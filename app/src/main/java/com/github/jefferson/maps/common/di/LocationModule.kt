package com.github.jefferson.maps.common.di

import com.google.android.gms.location.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationModule = module {

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<LocationRequest> {
        LocationRequest.create()
            .setInterval(3000)
            .setFastestInterval(1500)
            .setSmallestDisplacement(10f)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    single<LocationSettingsRequest> {
        LocationSettingsRequest.Builder().addLocationRequest(get()).setAlwaysShow(true).build()
    }

    single<SettingsClient> {
        LocationServices.getSettingsClient(androidContext())
    }
}