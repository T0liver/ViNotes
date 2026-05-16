package hu.toliver.vinotes.data.repository

import hu.toliver.vinotes.data.dao.WineDao
import hu.toliver.vinotes.data.local.entity.toDomain
import hu.toliver.vinotes.data.local.entity.toEntity
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WineRepositoryImpl @Inject constructor(
    private val dao: WineDao
) : WineRepository {

    override fun getAll(): Flow<List<Wine>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun searchWines(query: String): Flow<List<Wine>> =
        dao.searchWines(query).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: String): Result<Wine> = runCatching {
        dao.getById(id)?.toDomain() ?: error("Not found: $id")
    }

    override fun getFiltered(
        region: String?,
        colour: WineColour?,
        yearFrom: Int?,
        yearTo: Int?
    ): Flow<List<Wine>> =
        dao.getFiltered(
            region = region,
            colour = colour?.name,
            yearFrom = yearFrom,
            yearTo = yearTo
        ).map { entities -> entities.map { it.toDomain() } }

    override suspend fun insert(wine: Wine): Result<Unit> = runCatching {
        dao.insertOrReplace(wine.toEntity())
    }

    override suspend fun insertIfNotExists(wine: Wine): Result<Unit> = runCatching {
        dao.insertIfNotExists(wine.toEntity())
    }

    override suspend fun insertAll(wines: List<Wine>, fromCatalog: Boolean): Result<Unit> = runCatching {
        dao.insertAll(wines.map { it.toEntity(isFromCatalog = fromCatalog) })
    }

    override suspend fun update(wine: Wine): Result<Unit> = runCatching {
        dao.update(wine.toEntity())
    }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        dao.deleteById(id)
    }

    override suspend fun deleteAll(ids: List<String>): Result<Unit> = runCatching {
        dao.deleteByIds(ids)
    }

    override suspend fun deleteAll(): Result<Unit> = runCatching {
        dao.deleteAll()
    }
}