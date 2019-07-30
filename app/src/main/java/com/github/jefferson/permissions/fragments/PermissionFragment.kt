package com.github.jefferson.permissions.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.jefferson.permissions.models.PermissionResult
import java.util.*

class PermissionFragment : Fragment() {

    companion object {

        const val LIST_PERMISSIONS = "list_permissions"

        const val REQUEST_CODE = 84

        fun newInstance(permissions: Array<out String>, onResponse: (permissionResult: PermissionResult) -> Unit): PermissionFragment {
            val params = Bundle()
            params.putStringArray(LIST_PERMISSIONS, permissions)
            val fragment = PermissionFragment()
            fragment.arguments = params
            fragment.callback = onResponse
            return fragment
        }
    }

    init {
        retainInstance = true
    }

    private val permissions: Array<out String>
        get() = arguments?.getStringArray(LIST_PERMISSIONS) ?: Array(0) { "" }

    private var startTime: Long = 0

    var callback: ((permissionResult: PermissionResult) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        startTime = Calendar.getInstance().timeInMillis
        requestPermissions(permissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && permissions.isNotEmpty()) {
            val permissionResult = PermissionResult(
                isFromUser = Calendar.getInstance().timeInMillis - startTime > 250
            )
            permissions.forEachIndexed { index, permission ->
                when {
                    grantResults[index] == PackageManager.PERMISSION_GRANTED -> permissionResult.acceptedPermissions.add(permission)
                    shouldShowRequestPermissionRationale(permission) -> permissionResult.rejectedPermissions.add(permission)
                    else -> permissionResult.alwaysRejectedPermissions.add(permission)
                }
            }
            callback?.invoke(permissionResult)
            fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        }
    }
}