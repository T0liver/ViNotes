package hu.toliver.winotes.domain.repository

import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.model.enums.WineColour
import kotlinx.coroutines.flow.Flow

interface WineRepository {
    fun getAll(): Flow<List<Wine>>
    suspend fun getById(id: String): Result<Wine>
    fun getFiltered(
        region: String? = null,
        colour: WineColour? = null,
        yearFrom: Int? = null,
        yearTo: Int? = null
    ): Flow<List<Wine>>
    suspend fun insert(wine: Wine): Result<Unit>
    suspend fun insertAll(wines: List<Wine>, fromCatalog: Boolean): Result<Unit>
    suspend fun insertIfNotExists(wine: Wine): Result<Unit>
    suspend fun update(wine: Wine): Result<Unit>
    suspend fun delete(id: String): Result<Unit>
    suspend fun deleteAll(ids: List<String>): Result<Unit>
}