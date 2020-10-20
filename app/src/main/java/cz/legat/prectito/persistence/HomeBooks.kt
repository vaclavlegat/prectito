package cz.legat.prectito.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.legat.core.model.Book
import java.util.Date

const val POPULAR = "POPULAR"
const val NEW = "NEW"

@Entity
data class HomeBooks(@PrimaryKey(autoGenerate = true) var id: Int = 0, val timestamp: Date, val books: List<Book>, val type: String)