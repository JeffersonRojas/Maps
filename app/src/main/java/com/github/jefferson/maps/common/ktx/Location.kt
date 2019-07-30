package com.github.jefferson.maps.common.ktx

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun Location.toLatLong(): LatLng = LatLng(latitude, longitude)

fun Location.toMarker(): MarkerOptions = MarkerOptions().position(toLatLong())
