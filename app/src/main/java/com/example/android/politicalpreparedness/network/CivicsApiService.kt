package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    @GET("elections")
    suspend fun getElections(): ElectionResponse

    @GET("elections")
    suspend fun getElectionsRaw(): String

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Int
    ): VoterInfoResponse

    @GET("voterinfo")
    suspend fun getVoterInfoRaw(
        @Query("address") address: String,
        @Query("electionId") electionId: Int
    ): String

    @GET("representatives")
    suspend fun getRepresentatives(
        @Query("address") address: String
    ): RepresentativeResponse

    @GET("representatives")
    suspend fun getRepresentativesRaw(
        @Query("address") address: String
    ): String
}

object CivicsApi {
    private val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }

    suspend fun getElections() = retrofitService.getElections()
    suspend fun getElectionsRaw() = retrofitService.getElectionsRaw()

    suspend fun getVoterInfo(address: String, id: Int) = retrofitService.getVoterInfo(address, id)
    suspend fun getVoterInfoRaw(address: String, id: Int) =
        retrofitService.getVoterInfoRaw(address, id)

    suspend fun getRepresentatives(address: String) = retrofitService.getRepresentatives(address)
    suspend fun getRepresentativesRaw(address: String) =
        retrofitService.getRepresentativesRaw(address)
}