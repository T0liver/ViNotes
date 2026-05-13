package hu.toliver.vinotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.toliver.vinotes.data.dao.SyncMetadataDao
import hu.toliver.vinotes.data.dao.TasteDao
import hu.toliver.vinotes.data.dao.WineDao
import hu.toliver.vinotes.data.local.converters.DateConverter
import hu.toliver.vinotes.data.local.converters.ListConverter
import hu.toliver.vinotes.data.local.entity.SyncMetadataEntity
import hu.toliver.vinotes.data.local.entity.TasteEntity
import hu.toliver.vinotes.data.local.entity.WineEntity

@Database(
    entities = [
        WineEntity::class,
        TasteEntity::class,
        SyncMetadataEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, ListConverter::class)
abstract class WineDatabase : RoomDatabase() {
    abstract fun wineDao(): WineDao
    abstract fun tasteDao(): TasteDao
    abstract fun syncMetadataDao(): SyncMetadataDao
}