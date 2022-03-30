package com.example.android.politicalpreparedness.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.database.Constants

@Entity(tableName = Constants.VOTER_INFO_TABLE_NAME)
data class VoterInfo(
    @PrimaryKey val id: Int,
    val stateName: String,
    val votingLocationUrl: String?,
    val ballotInformationUrl: String?
)