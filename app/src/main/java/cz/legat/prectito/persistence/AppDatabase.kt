package cz.legat.prectito.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedBook::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedBookDao(): SavedBookDao
}