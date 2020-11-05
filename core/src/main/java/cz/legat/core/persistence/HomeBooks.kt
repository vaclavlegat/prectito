package cz.legat.core.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.legat.core.model.Book
import java.util.Date

const val POPULAR = "POPULAR"
const val NEW = "NEW"

@Entity
data class LocalOverview(@PrimaryKey(autoGenerate = true) var id: Int = 0, val timestamp: Date = Date(), val popularBooks: List<Book>, val newBooks: List<Book>, val eBooks: List<Book>)