package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.database.model.VoterInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface VoterInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(voterInfo: VoterInfo)

    @Query("SELECT * FROM ${Constants.VOTER_INFO_TABLE_NAME} WHERE id = :id")
    fun get(id: Int): Flow<VoterInfo>
}