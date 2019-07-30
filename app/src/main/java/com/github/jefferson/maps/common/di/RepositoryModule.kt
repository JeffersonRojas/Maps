package com.github.jefferson.maps.common.di

import com.github.jefferson.maps.common.repositories.LocationRepository
import com.github.jefferson.maps.common.repositories.PlacesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LocationRepository()
    }

    single {
        PlacesRepository()
    }
}