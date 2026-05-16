package hu.toliver.vinotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.toliver.vinotes.data.local.entity.TasteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasteDao {
    @Query("SELECT * FROM tastings ORDER BY date DESC")
    fun getAll(): Flow<List<TasteEntity>>

    @Query("SELECT * FROM tastings WHERE wine_id = :wineId ORDER BY date DESC")
    fun getByWineId(wineId: String): Flow<List<TasteEntity>>

    @Query("SELECT * FROM tastings WHERE id = :id")
    suspend fun getById(id: String): TasteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taste: TasteEntity)

    @Update
    suspend fun update(taste: TasteEntity)

    @Query("DELETE FROM tastings WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM tastings")
    suspend fun deleteAll()

    // Stats lekérdezések
    @Query("SELECT * FROM tastings ORDER BY rating DESC LIMIT :limit")
    fun getTopRated(limit: Int): Flow<List<TasteEntity>>
}