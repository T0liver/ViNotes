package hu.toliver.vinotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.toliver.vinotes.data.local.entity.WineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WineDao {
    @Query("SELECT * FROM wines ORDER BY name ASC")
    fun getAll(): Flow<List<WineEntity>>

    @Query("SELECT * FROM wines WHERE id = :id")
    suspend fun getById(id: String): WineEntity?

    @Query("""
        SELECT * FROM wines 
        WHERE (:region IS NULL OR region = :region)
        AND (:colour IS NULL OR colour = :colour)
        AND (:yearFrom IS NULL OR year >= :yearFrom)
        AND (:yearTo IS NULL OR year <= :yearTo)
        ORDER BY name ASC
    """)
    fun getFiltered(
        region: String? = null,
        colour: String? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null
    ): Flow<List<WineEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(wine: WineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(wine: WineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wines: List<WineEntity>)

    @Update
    suspend fun update(wine: WineEntity)

    @Query("DELETE FROM wines WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM wines WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)
}