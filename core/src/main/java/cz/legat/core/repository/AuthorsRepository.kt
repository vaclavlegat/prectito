package cz.legat.core.repository

import cz.legat.core.model.Author
import cz.legat.core.model.Book

interface AuthorsRepository {
    fun getAuthors(page: Int, id: String? = null): List<Author>
    fun getAuthorBooks(page: Int, id: String? = null): List<Book>
    suspend fun getAuthor(id: String): Author?
    suspend fun searchAuthor(query: String): List<Author>
}