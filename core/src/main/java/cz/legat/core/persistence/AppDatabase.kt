package cz.legat.core.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SavedBook::class, LocalOverview::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedBookDao(): SavedBookDao
    abstract fun overviewBooksDao(): OverviewDao
}