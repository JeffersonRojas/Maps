package com.github.jefferson.permissions.ktx

import androidx.fragment.app.FragmentActivity
import com.github.jefferson.permissions.fragments.PermissionFragment
import com.github.jefferson.permissions.models.PermissionRequest

fun FragmentActivity?.askPermission(vararg permissions: String): PermissionRequest {
    val permissionRequest = PermissionRequest()
    validatePermissions(permissions, permissionRequest)
    return permissionRequest
}

fun FragmentActivity?.askPermission(vararg permissions: String, onAccepted: () -> Unit): PermissionRequest {
    val permissionRequest = PermissionRequest(onAccepted = onAccepted)
    validatePermissions(permissions, permissionRequest)
    return permissionRequest
}

fun FragmentActivity?.validatePermissions(permissions: Array<out String>, permissionRequest: PermissionRequest) {
    val fragment = PermissionFragment.newInstance(permissions) {
        if (it.rejectedPermissions.isEmpty() && it.alwaysRejectedPermissions.isEmpty()) {
            permissionRequest.onAccepted()
        }
        if (it.rejectedPermissions.isNotEmpty() || it.alwaysRejectedPermissions.isNotEmpty()) {
            permissionRequest.onDenied()
        }
        if (it.alwaysRejectedPermissions.isNotEmpty()) {
            permissionRequest.onAlwaysDenied(it.isFromUser)
        }
    }
    if (this != null && !this.isFinishing) {
        runOnUiThread {
            supportFragmentManager.beginTransaction().add(fragment, PermissionFragment::class.java.name).commitAllowingStateLoss()
        }
    }
}