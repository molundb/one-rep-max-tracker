package net.martinlundberg.onerepmaxtracker

import javax.inject.Inject
import javax.inject.Singleton

interface ClockService {
    fun getCurrentTimeMillis(): Long
}

@Singleton
class ClockServiceImpl @Inject constructor() : ClockService {
    override fun getCurrentTimeMillis() = System.currentTimeMillis()
}

