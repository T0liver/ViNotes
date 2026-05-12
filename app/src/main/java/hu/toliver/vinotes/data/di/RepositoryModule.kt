package hu.toliver.vinotes.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.toliver.vinotes.data.repository.CatalogSyncRepositoryImpl
import hu.toliver.vinotes.data.repository.TasteRepositoryImpl
import hu.toliver.vinotes.data.repository.WineRepositoryImpl
import hu.toliver.vinotes.domain.repository.CatalogSyncRepository
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
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