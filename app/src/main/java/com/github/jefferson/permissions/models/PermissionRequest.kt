package com.github.jefferson.permissions.models

class PermissionRequest(

    internal var onAccepted: () -> Unit = {},

    internal var onDenied: () -> Unit = {},

    internal var onAlwaysDenied: (isFromUser: Boolean) -> Unit = {}

) {

    fun onAccepted(onAccepted: () -> Unit): PermissionRequest {
        this.onAccepted = onAccepted
        return this
    }

    fun onDenied(onDenied: () -> Unit): PermissionRequest {
        this.onDenied = onDenied
        return this
    }

    fun onAlwaysDenied(onAlwaysDenied: (isFromUser: Boolean) -> Unit): PermissionRequest {
        this.onAlwaysDenied = onAlwaysDenied
        return this
    }
}