package hu.toliver.vinotes.domain.repository

import hu.toliver.vinotes.domain.model.Taste
import kotlinx.coroutines.flow.Flow

interface TasteRepository {
    fun getAll(): Flow<List<Taste>>
    fun getByWineId(wineId: String): Flow<List<Taste>>
    suspend fun getById(id: String): Result<Taste>
    suspend fun save(taste: Taste): Result<Unit>
    suspend fun update(taste: Taste): Result<Unit>
    suspend fun delete(id: String): Result<Unit>
}