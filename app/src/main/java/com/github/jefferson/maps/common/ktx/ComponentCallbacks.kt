package com.github.jefferson.maps.common.ktx

import android.content.ComponentCallbacks
import org.koin.android.ext.android.get
import retrofit2.Retrofit

inline fun <reified T> ComponentCallbacks.injectApi(): Lazy<T> = lazy {
    get<Retrofit>().create(T::class.java)
}

inline fun <reified T> ComponentCallbacks.getApi(): T = get<Retrofit>().create(T::class.java)