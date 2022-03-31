package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.model.VoterInfo
import com.example.android.politicalpreparedness.election.repository.ElectionsRepository
import com.example.android.politicalpreparedness.election.repository.VoterInfoRepository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class VoterInfoViewModel(
    private val application: Application,
    private val voterInfoRepository: VoterInfoRepository,
    private val electionsRepository: ElectionsRepository
) :
    ViewModel() {
    companion object {
        private const val TAG = "VoterInfoViewModel"
        private const val DEFAULT_STATE = "la"
    }

    private val _selectedElection = MutableLiveData<Election>()
    val selectedElection: LiveData<Election>
        get() = _selectedElection

    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo


    fun refresh(data: Election) {
        _selectedElection.value = data
        refreshVoterInfo(data)
    }

    private fun refreshVoterInfo(election: Election) {
        viewModelScope.launch {
            val state = election.division.state.ifEmpty { DEFAULT_STATE }
            val address = "${state},${election.division.country}"
            try {
                voterInfoRepository.refreshVoterInfo(election.id, address)
                voterInfoRepository.getVoterInfo(election.id).collect {
                    _voterInfo.value = it
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error requesting data", e)
                voterInfoRepository.getVoterInfo(election.id).collect {
                    _voterInfo.value = it
                }
            }

        }
    }

    private fun refreshIsElectionSaved(election: Election) {
        _selectedElection.value = election
    }

    fun onFollowButtonClick() {
        _selectedElection.value?.let {
            viewModelScope.launch {
                Log.i(TAG, "Updating election ${it.id}")
                val updatedElection = electionsRepository.updateSavedStatus(it)
                Log.i(TAG, "Election ${it.id} status updated to ${updatedElection.isSaved}")
                refreshIsElectionSaved(updatedElection)
            }
        }
    }

}