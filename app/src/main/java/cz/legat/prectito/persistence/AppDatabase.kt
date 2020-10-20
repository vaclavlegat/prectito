package cz.legat.prectito.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SavedBook::class, HomeBooks::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedBookDao(): SavedBookDao
    abstract fun homeBooksDao(): HomeBooksDao
}