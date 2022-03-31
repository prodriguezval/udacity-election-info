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

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

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

    fun onFollowButtonClick() {
        viewModelScope.launch {
            val election = _selectedElection.value

            election?.let {
                it.isSaved = !it.isSaved
            }

            _selectedElection.value = election!!
//            _selectedElection.value?.let {
//                if(isElectionSaved.value == true) {
//                    repository.deleteSavedElection(it)
//                } else {
//                    repository.insertSavedElection(it)
//                }
//                refreshIsElectionSaved(it)
//            }
        }
    }

}