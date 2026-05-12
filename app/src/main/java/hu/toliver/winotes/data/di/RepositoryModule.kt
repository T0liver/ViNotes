package hu.toliver.winotes.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.toliver.winotes.data.repository.CatalogSyncRepositoryImpl
import hu.toliver.winotes.data.repository.TasteRepositoryImpl
import hu.toliver.winotes.data.repository.WineRepositoryImpl
import hu.toliver.winotes.domain.repository.CatalogSyncRepository
import hu.toliver.winotes.domain.repository.TasteRepository
import hu.toliver.winotes.domain.repository.WineRepository
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWineRepository(
        impl: WineRepositoryImpl
    ): WineRepository

    @Binds
    @Singleton
    abstract fun bindTasteRepository(
        impl: TasteRepositoryImpl
    ): TasteRepository

    @Binds
    @Singleton
    abstract fun bindCatalogSyncRepository(
        impl: CatalogSyncRepositoryImpl
    ): CatalogSyncRepository
}