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
    }

    private val repository = RepresentativeRepository(CivicsApi)

    private val _address = uiState.getLiveData(
        "address",
        Address("", "", "", "New York", "")
    )
    val address: LiveData<Address>
        get() = _address

    private val _states = MutableLiveData<List<String>>()
    val states: LiveData<List<String>>
        get() = _states

    private val _representatives =
        uiState.getLiveData<List<Representative>>("representatives", emptyList())
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val selectedStateIndex = uiState.getLiveData("state", 0)

    init {
        _states.value = app.resources.getStringArray(R.array.states).toList()
    }

    private fun refreshRepresentatives() {
        viewModelScope.launch {
            try {
                _address.value!!.state = getSelectedState(selectedStateIndex.value!!)
                val addressStr = address.value!!.toFormattedString()
                repository.getRepresentatives(addressStr).collect {
                    _representatives.value = it
                    uiState.set("representatives", it)
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
            uiState.set("state", stateIndex)
            _address.value = address
            uiState.set("address", address)
            refreshRepresentatives()
        } else {
            Log.e(TAG, "Current location isn't US")
        }
    }
}
