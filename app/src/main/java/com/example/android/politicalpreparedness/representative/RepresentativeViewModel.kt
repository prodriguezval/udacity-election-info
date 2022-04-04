package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.representative.repository.RepresentativeRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val app: Application, private val uiState: SavedStateHandle) :
    ViewModel() {
    companion object {
        private const val TAG = "RepresentativeViewModel"
        private const val STATE_REPRESENTATIVES = "representatives"
        private const val STATE_SELECTED_US_STATE = "state"
        private const val STATE_ADDRESS_LINE_1 = "address_line_1"
        private const val STATE_ADDRESS_LINE_2 = "address_line_2"
        private const val STATE_ADDRESS_CITY = "address_city"
        private const val STATE_ADDRESS_ZIP_CODE = "address_zip"
    }

    private val repository = RepresentativeRepository(CivicsApi)

    private val address = Address("", "", "", "", "")

    private val _states = MutableLiveData<List<String>>()
    val states: LiveData<List<String>>
        get() = _states

    private val _representatives =
        uiState.getLiveData<List<Representative>>(STATE_REPRESENTATIVES, emptyList())
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val selectedStateIndex = uiState.getLiveData(STATE_SELECTED_US_STATE, 0)

    val addressLine1 = uiState.getLiveData(STATE_ADDRESS_LINE_1, "")
    val addressLine2 = uiState.getLiveData(STATE_ADDRESS_LINE_2, "")
    val city = uiState.getLiveData(STATE_ADDRESS_CITY, "")
    val zipCode = uiState.getLiveData(STATE_ADDRESS_ZIP_CODE, "")

    init {
        _states.value = app.resources.getStringArray(R.array.states).toList()
        _representatives.value = uiState.get(STATE_REPRESENTATIVES)
    }

    private fun refreshRepresentatives() {
        viewModelScope.launch {
            try {
                address.state = getSelectedState(selectedStateIndex.value!!)
                val addressStr = address.toFormattedString()
                repository.getRepresentatives(addressStr).collect {
                    _representatives.value = it
                    uiState.set(STATE_REPRESENTATIVES, it)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }
    }

    private fun getSelectedState(stateIndex: Int): String {
        return states.value!!.toList()[stateIndex]
    }

    fun onSearchButtonClick() {
        refreshRepresentatives()
    }

    fun refreshByCurrentLocation(address: Address) {
        val stateIndex = _states.value?.indexOf(address.state)
        Log.i(TAG, "state index $stateIndex for ${address.state}")
        if (stateIndex != null && stateIndex >= 0) {
            selectedStateIndex.value = stateIndex!!
            uiState.set(STATE_SELECTED_US_STATE, stateIndex)
            uiState.set(STATE_ADDRESS_LINE_1, address.line1)
            uiState.set(STATE_ADDRESS_LINE_2, address.line2)
            uiState.set(STATE_ADDRESS_CITY, address.city)
            uiState.set(STATE_ADDRESS_ZIP_CODE, address.zip)
            uiState.set(STATE_SELECTED_US_STATE, stateIndex)
            refreshRepresentatives()
        } else {
            Log.e(TAG, "Current location isn't US")
        }
    }

    fun updateAddressLine1(s: CharSequence, start: Int, before: Int, count: Int) {
        val text = s.toString()
        addressLine1.value = text
        uiState.set(STATE_ADDRESS_LINE_1, text)
    }

    fun updateAddressLine2(s: CharSequence, start: Int, before: Int, count: Int) {
        val text = s.toString()
        addressLine2.value = text
        uiState.set(STATE_ADDRESS_LINE_2, text)
    }

    fun updateCity(s: CharSequence, start: Int, before: Int, count: Int) {
        val text = s.toString()
        city.value = text
        uiState.set(STATE_ADDRESS_CITY, text)
    }

    fun updateZip(s: CharSequence, start: Int, before: Int, count: Int) {
        val text = s.toString()
        zipCode.value = text
        uiState.set(STATE_ADDRESS_ZIP_CODE, text)
    }
}
