package com.example.android.politicalpreparedness.election.repository

import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.database.model.VoterInfo
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.toVoterInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VoterInfoRepository(
    private val voterInfoDatabase: VoterInfoDatabase,
    private val api: CivicsApi
) {
    suspend fun refreshVoterInfo(id: Int, address: String) {
        withContext(Dispatchers.IO) {
            val response = api.getVoterInfo(address, id)
            val voterInfo = response.toVoterInfo(id)
            voterInfo?.run {
                voterInfoDatabase.insert(this)
            }
        }
    }

    suspend fun getVoterInfo(id: Int): Flow<VoterInfo> {
        var voterInfo: Flow<VoterInfo>
        withContext(Dispatchers.IO) {
            val data = voterInfoDatabase.get(id)
            voterInfo = data
        }

        return voterInfo
    }
}