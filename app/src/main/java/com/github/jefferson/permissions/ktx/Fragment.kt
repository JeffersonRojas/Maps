package com.github.jefferson.permissions.ktx

import androidx.fragment.app.Fragment
import com.github.jefferson.permissions.models.PermissionRequest

fun Fragment.askPermission(vararg permissions: String): PermissionRequest {
    val permissionRequest = PermissionRequest()
    activity.validatePermissions(permissions, permissionRequest)
    return permissionRequest
}

fun Fragment.askPermission(vararg permissions: String, onAccepted: () -> Unit): PermissionRequest {
    val permissionRequest = PermissionRequest(onAccepted = onAccepted)
    activity.validatePermissions(permissions, permissionRequest)
    return permissionRequest
}