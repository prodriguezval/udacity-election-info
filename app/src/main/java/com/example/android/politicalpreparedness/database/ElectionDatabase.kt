package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.models.Election

@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase: RoomDatabase() {

    abstract val electionDao: ElectionDao

    companion object {

        @Volatile
        private var INSTANCE: ElectionDatabase? = null

        fun getInstance(context: Context): ElectionDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ElectionDatabase::class.java,
                            "election_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

    suspend fun getAll() = electionDao.getAll()
    suspend fun get(id: Int) = electionDao.get(id)
    suspend fun insertAll(elections: List<Election>) = electionDao.insertAll(elections)
    suspend fun insert(election: Election) = electionDao.insert(election)
    suspend fun getAllSaved() = electionDao.getAllSaved()
    suspend fun delete(election: Election) = electionDao.delete(election)

}