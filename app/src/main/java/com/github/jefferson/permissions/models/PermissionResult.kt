package com.github.jefferson.permissions.models

import java.util.ArrayList

data class PermissionResult(

    val acceptedPermissions: ArrayList<String> = ArrayList(),

    val rejectedPermissions: ArrayList<String> = ArrayList(),

    val alwaysRejectedPermissions: ArrayList<String> = ArrayList(),

    var isFromUser: Boolean = true

)