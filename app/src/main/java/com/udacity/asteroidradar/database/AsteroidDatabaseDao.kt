package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDatabaseDao {
    @Query("SELECT * FROM asteroiddb ORDER BY closeApproachDate DESC")
    fun getAllAsteroids(): Flow<List<Asteroid>>


    @Query("SELECT * FROM asteroiddb ORDER BY closeApproachDate DESC")
    fun getAllLiveAsteroids(): LiveData<List<AsteroidDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: AsteroidDB)

    @Query("SELECT * FROM asteroiddb WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("DELETE FROM asteroiddb WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int
}