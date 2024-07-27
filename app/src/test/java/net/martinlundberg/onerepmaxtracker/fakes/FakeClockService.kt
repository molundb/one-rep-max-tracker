package net.martinlundberg.onerepmaxtracker.fakes

import net.martinlundberg.onerepmaxtracker.ClockService

class FakeClockService : ClockService {
    override fun getCurrentTimeMillis(): Long {
        return 100L
    }
}