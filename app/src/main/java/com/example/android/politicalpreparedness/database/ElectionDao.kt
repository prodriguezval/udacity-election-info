package com.example.android.politicalpreparedness.database


import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(elections: List<Election>)

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME}")
    fun getAll(): Flow<List<Election>>

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} WHERE isSaved = 1")
    fun getAllSaved(): Flow<List<Election>>

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} WHERE id = :id")
    fun get(id: Int): Flow<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)

    @Delete
    suspend fun delete(election: Election)

    @Query("DELETE FROM ${Constants.ELECTION_TABLE_NAME}")
    suspend fun clear()

}