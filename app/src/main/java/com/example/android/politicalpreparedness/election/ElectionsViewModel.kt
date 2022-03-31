package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.repository.ElectionsRepository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val application: Application,
    private val repository: ElectionsRepository
) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    init {
        viewModelScope.launch {
            repository.getUpcomingElections().collect {
                _upcomingElections.value = it
            }

            repository.getSavedElections().collect {
                _savedElections.value = it
                Log.i("ElectionsViewModel", "Saved election has ${it.size} registers")
            }
        }
    }

}