package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

interface OneRepMaxRepository {
    fun getMovementDetail(id: Long): Flow<MovementDetail>
    suspend fun addOneRM(oneRM: OneRMInfo, weightUnit: String)
    fun getOneRM(id: Long): Flow<OneRMInfo>
    suspend fun deleteOneRM(id: Long)
}