package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

class RepresentativeViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultState: Bundle?
) : AbstractSavedStateViewModelFactory(owner, defaultState) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(application, handle) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }

}