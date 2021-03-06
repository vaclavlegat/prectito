package cz.legat.core.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedBookDao {
    @Query("SELECT * FROM savedBook")
    suspend fun getAll(): List<SavedBook>

    @Insert
    fun insertAll(vararg users: SavedBook)

    @Delete
    fun delete(savedBook: SavedBook)
}