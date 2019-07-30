package com.github.jefferson.maps.common.ktx

import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.Retrofit

inline fun <reified T> KoinComponent.injectApi(): Lazy<T> = lazy {
    get<Retrofit>().create(T::class.java)
}

inline fun <reified T> KoinComponent.getApi(): T = get<Retrofit>().create(T::class.java)