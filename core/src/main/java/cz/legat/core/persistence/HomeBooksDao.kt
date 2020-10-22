package cz.legat.core.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomeBooksDao {
    @Query("SELECT * FROM homebooks where type = :type LIMIT 1")
    suspend fun getByType(type: String): HomeBooks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(homeBooks: HomeBooks)

    @Delete
    fun delete(homeBooks: HomeBooks)
}