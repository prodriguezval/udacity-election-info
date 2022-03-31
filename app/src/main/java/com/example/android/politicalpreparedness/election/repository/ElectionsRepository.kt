package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext


class ElectionsRepository(
    private val electionDatabase: ElectionDatabase,
    private val api: CivicsApi
) {

    suspend fun refreshElections(): Flow<List<Election>> {
        var elections: Flow<List<Election>>
        withContext(Dispatchers.IO) {
            val electionResponse = api.getElections().elections
            electionDatabase.insertAll(electionResponse)
            elections = flowOf(electionResponse)
        }

        return elections
    }

    suspend fun updateSavedStatus(election: Election): Election {
        election.isSaved = !election.isSaved
        withContext(Dispatchers.IO) {
            electionDatabase.insert(election)
        }

        return election
    }
}