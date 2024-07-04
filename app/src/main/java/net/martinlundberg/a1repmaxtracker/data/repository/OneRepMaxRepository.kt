package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

interface OneRepMaxRepository {
    fun getMovementDetail(id: Long): Flow<MovementDetail>
    suspend fun addOneRM(oneRM: OneRMInfo)
    suspend fun getOneRM(id: Long): OneRMInfo
    suspend fun deleteOneRM(id: Long)
}