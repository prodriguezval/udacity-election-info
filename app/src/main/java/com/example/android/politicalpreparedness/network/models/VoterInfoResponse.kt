package com.example.android.politicalpreparedness.network.models

import com.example.android.politicalpreparedness.database.model.VoterInfo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse(
    val election: Election,
    val state: List<State>? = null
)

fun VoterInfoResponse.toVoterInfo(id: Int): VoterInfo? {
    var voterInfo: VoterInfo? = null
    val state = this.state

    if (state?.isNotEmpty() == true) {
        val votingLocationUrl: String? =
            state.first().electionAdministrationBody.votingLocationFinderUrl?.run {
                this
            }

        val ballotInfoUrl: String? =
            state.first().electionAdministrationBody.ballotInfoUrl?.run {
                this
            }

        voterInfo = VoterInfo(
            id,
            state[0].name,
            votingLocationUrl,
            ballotInfoUrl
        )
    }

    return voterInfo
}