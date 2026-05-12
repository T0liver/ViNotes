package hu.toliver.winotes.data.repository

import hu.toliver.winotes.data.dao.TasteDao
import hu.toliver.winotes.data.local.entity.toDomain
import hu.toliver.winotes.data.local.entity.toEntity
import hu.toliver.winotes.domain.model.Taste
import hu.toliver.winotes.domain.repository.TasteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TasteRepositoryImpl @Inject constructor(
    private val dao: TasteDao
) : TasteRepository {

    override fun getAll(): Flow<List<Taste>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getByWineId(wineId: String): Flow<List<Taste>> =
        dao.getByWineId(wineId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: String): Result<Taste> = runCatching {
        dao.getById(id)?.toDomain() ?: error("Tasting not found: $id")
    }

    override suspend fun save(taste: Taste): Result<Unit> = runCatching {
        val id = UUID.randomUUID().toString()
        dao.insert(taste.toEntity(id = id))
    }

    override suspend fun update(taste: Taste): Result<Unit> = runCatching {
        dao.update(taste.toEntity(id = taste.wineId))
    }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        dao.deleteById(id)
    }
}