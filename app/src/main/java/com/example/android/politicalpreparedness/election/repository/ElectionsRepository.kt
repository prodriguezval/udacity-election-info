package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ElectionsRepository(private val api: CivicsApi) {

    suspend fun refreshElections(): List<Election> {
        var elections: List<Election>
        withContext(Dispatchers.IO) {
            elections = api.getElections().elections
        }

        return elections
    }
}