package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.representative.repository.RepresentativeRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepresentativeViewModel(app: Application) : ViewModel() {
    companion object {
        private const val TAG = "RepresentativeViewModel"
    }

    private val repository = RepresentativeRepository(CivicsApi)

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _states = MutableLiveData<List<String>>()
    val states: LiveData<List<String>>
        get() = _states

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val selectedStateIndex = MutableLiveData<Int>()

    init {
        _address.value = Address("", "", "", "New York", "")
        _states.value = app.resources.getStringArray(R.array.states).toList()
    }

    private fun refreshRepresentatives() {
        viewModelScope.launch {
            try {
                _address.value!!.state = getSelectedState(selectedStateIndex.value!!)
                val addressStr = address.value!!.toFormattedString()
                repository.getRepresentatives(addressStr).collect {
                    _representatives.value = it
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
            _address.value = address
            refreshRepresentatives()

        } else {
            Log.e(TAG, "Current location isn't US")
        }
    }
}
