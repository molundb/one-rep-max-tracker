package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

interface OneRepMaxRepository {
    suspend fun getMovementDetail(id: Long): MovementDetail
    suspend fun addOneRM(oneRM: OneRMInfo)
    suspend fun getOneRM(id: Long): OneRMInfo
    suspend fun deleteOneRM(id: Long)
}