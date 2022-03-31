package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.election.repository.ElectionsRepository
import com.example.android.politicalpreparedness.election.repository.VoterInfoRepository

class VoterInfoViewModelFactory(
    private val voterInfoRepository: VoterInfoRepository,
    private val electionsRepository: ElectionsRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(application, voterInfoRepository, electionsRepository) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }

}