package net.martinlundberg.a1repmaxtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.a1repmaxtracker.data.database.model.ResultEntity
import net.martinlundberg.a1repmaxtracker.data.database.util.DateConverter

@Database(
    entities = [MovementEntity::class, ResultEntity::class],
    version = 1,
)
@TypeConverters(
    DateConverter::class,
)
abstract class OneRepMaxTrackerDatabase : RoomDatabase() {
    abstract fun movementDao(): MovementDao
    abstract fun resultDao(): ResultDao
}
