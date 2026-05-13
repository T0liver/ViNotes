package hu.toliver.vinotes.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.toliver.vinotes.data.WineDatabase
import hu.toliver.vinotes.data.dao.SyncMetadataDao
import hu.toliver.vinotes.data.dao.TasteDao
import hu.toliver.vinotes.data.dao.WineDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWineDatabase(@ApplicationContext context: Context): WineDatabase =
        Room.databaseBuilder(
            context,
            WineDatabase::class.java,
            "winote.db"
        ).build()

    @Provides
    @Singleton
    fun provideWineDao(database: WineDatabase): WineDao =
        database.wineDao()

    @Provides
    @Singleton
    fun provideTasteDao(database: WineDatabase): TasteDao =
        database.tasteDao()

    @Provides
    @Singleton
    fun provideSyncMetadataDao(database: WineDatabase): SyncMetadataDao =
        database.syncMetadataDao()
}