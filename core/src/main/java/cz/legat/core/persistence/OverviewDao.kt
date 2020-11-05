package cz.legat.core.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.legat.core.model.Overview

@Dao
interface OverviewDao {
    @Query("SELECT * FROM localoverview LIMIT 1")
    suspend fun getOverview(): LocalOverview?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(overview: LocalOverview)

    @Delete
    fun delete(overview: LocalOverview)
}