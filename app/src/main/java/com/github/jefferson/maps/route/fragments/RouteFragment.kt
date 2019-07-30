package com.github.jefferson.maps.route.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.jefferson.maps.R
import com.github.jefferson.maps.databinding.FragmentRouteBinding
import com.github.jefferson.maps.route.viewmodels.RouteViewModel
import com.github.jefferson.permissions.ktx.askPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.SupportMapFragment
import io.reactivex.Completable

class RouteFragment : Fragment(), RouteViewModel.Listener {

    companion object {

        const val GPS_RC = 1001

    }

    private val viewModel: RouteViewModel by viewModels()

    lateinit var binding: FragmentRouteBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.listener = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_route, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(viewModel::onMapReady)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (GPS_RC == requestCode && resultCode == Activity.RESULT_OK) {
            viewModel.startLocationUpdates()
        }
    }

    override fun checkGpsPermissions(): Completable = Completable.create {
        askPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).onAccepted {
            it.onComplete()
        }.onDenied {
            it.tryOnError(Exception("User reject location permission"))
        }.onAlwaysDenied { isFromUser ->
            it.tryOnError(Exception("User reject location permission"))
        }
    }

    override fun tryToResolveException(exception: ResolvableApiException) {
        exception.startResolutionForResult(activity, GPS_RC)
    }
}