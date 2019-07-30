package com.github.jefferson.maps.common.application

import androidx.multidex.MultiDexApplication
import com.github.jefferson.maps.common.di.locationModule
import com.github.jefferson.maps.common.di.networkModule
import com.github.jefferson.maps.common.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MapsApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MapsApplication)
            modules(locationModule)
            modules(networkModule)
            modules(repositoryModule)
        }
    }

}