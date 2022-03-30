package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.election.repository.ElectionsRepository

class ElectionsViewModelFactory(
    private val application: Application,
    private val electionsRepository: ElectionsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(application, electionsRepository) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }

}