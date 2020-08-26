package cz.legat.prectito.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedBookDao {
    @Query("SELECT * FROM savedBook")
    fun getAll(): List<SavedBook>

    @Insert
    fun insertAll(vararg users: SavedBook)

    @Delete
    fun delete(savedBook: SavedBook)
}