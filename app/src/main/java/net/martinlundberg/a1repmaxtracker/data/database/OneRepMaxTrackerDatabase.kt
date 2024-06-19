package net.martinlundberg.a1repmaxtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Database(entities = [MovementEntity::class, OneRMEntity::class], version = 1)
abstract class OneRepMaxTrackerDatabase : RoomDatabase() {
    abstract fun movementDao(): MovementDao
    abstract fun oneRMDao(): OneRMDao
}