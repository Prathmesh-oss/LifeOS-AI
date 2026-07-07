package com.lifeosai.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifeosai.app.data.entity.DNAEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DNADao {
    @Query("SELECT * FROM lifeos_dna LIMIT 1")
    fun getDNA(): Flow<DNAEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDNA(dna: DNAEntity)

    @Query("UPDATE lifeos_dna SET life_score = :score WHERE id = :dnaId")
    suspend fun updateLifeScore(dnaId: String, score: Int)
}
