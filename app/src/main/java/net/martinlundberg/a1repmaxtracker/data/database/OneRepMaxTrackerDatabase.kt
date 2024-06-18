package net.martinlundberg.a1repmaxtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity

@Database(entities = [MovementEntity::class], version = 1)
abstract class OneRepMaxTrackerDatabase : RoomDatabase() {
    abstract fun movementDao(): MovementDao
}