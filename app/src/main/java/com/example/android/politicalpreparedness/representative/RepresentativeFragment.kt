package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import java.util.*

class RepresentativeFragment : Fragment() {

    lateinit var binding: FragmentRepresentativeBinding
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var enableLocationSettingLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val representativeViewModelFactory =
            RepresentativeViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(
                this,
                representativeViewModelFactory
            )[RepresentativeViewModel::class.java]

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = RepresentativeListAdapter()
        binding.representativesRecyclerView.adapter = adapter
        viewModel.representatives.observe(viewLifecycleOwner) { representatives ->
            adapter.submitList(representatives)
        }

        binding.searchButton.setOnClickListener {
            hideKeyboard()
            viewModel.onSearchButtonClick()
        }

        registerLocationPermissionsCallback()
        registerEnableLocationCallback()
        binding.locationButton.setOnClickListener { requestLocationPermissions() }

        return binding.root
    }

    private fun registerEnableLocationCallback() {
        enableLocationSettingLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == RESULT_OK)
                getLocation()
            else {
                showSnackBar(R.string.location_required_error_msg)
            }
        }
    }

    private fun registerLocationPermissionsCallback() {

        requestLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            if (isGranted) {
                checkDeviceLocationSettingsAndGetLocation()
            } else {
                showSnackBar(R.string.location_required_error_msg)
            }
        }
    }

    private fun requestLocationPermissions() {

        if (isPermissionGranted()) {
            checkDeviceLocationSettingsAndGetLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isPermissionGranted(): Boolean {

        var granted = false
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            granted = true
        }

        return granted
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.let {

                    val address = geoCodeLocation(it.lastLocation)
                    viewModel.refreshByCurrentLocation(address)

                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )

    }

    private fun checkDeviceLocationSettingsAndGetLocation(resolve: Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {

                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    enableLocationSettingLauncher.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            } else {
                showSnackBar(R.string.location_required_error_msg)
            }
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                getLocation()
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        Log.i("GEOCODER", location.latitude.toString())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

}