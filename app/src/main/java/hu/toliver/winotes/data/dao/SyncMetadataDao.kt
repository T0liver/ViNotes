package hu.toliver.winotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.toliver.winotes.data.local.entity.SyncMetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncMetadataDao {

    // Mindig csak 1 sor van, id = 1
    @Query("SELECT * FROM sync_metadata WHERE id = 1")
    fun get(): Flow<SyncMetadataEntity?>

    @Query("SELECT * FROM sync_metadata WHERE id = 1")
    suspend fun getOnce(): SyncMetadataEntity?

    // REPLACE: ha még nincs sor, létrehozza; ha van, felülírja
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(metadata: SyncMetadataEntity)
}