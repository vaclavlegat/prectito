package cz.legat.core.repository

import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.core.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface AuthorsRepository {
    fun getAuthors(page: Int, id: String? = null): List<Author>
    fun getAuthorBooks(page: Int, id: String? = null): List<Book>
    suspend fun getAuthor(id: String): Author?
    fun searchAuthor(query: String): Flow<List<SearchResult>>
}