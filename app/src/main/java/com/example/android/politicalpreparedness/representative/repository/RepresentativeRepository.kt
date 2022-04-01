package com.example.android.politicalpreparedness.representative.repository

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class RepresentativeRepository(private val api: CivicsApi) {
    suspend fun getRepresentatives(addressStr: String): Flow<List<Representative>> {
        var representatives: List<Representative>
        withContext(Dispatchers.IO) {
            val representativeResponse = api.getRepresentatives(addressStr)

            representatives = representativeResponse.offices.flatMap { office ->
                office.getRepresentatives(representativeResponse.officials)
            }
        }
        return flowOf(representatives)
    }
}