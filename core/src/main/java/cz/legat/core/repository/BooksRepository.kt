package cz.legat.core.repository

import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.core.model.Overview
import cz.legat.core.model.SearchResult
import cz.legat.core.persistence.SavedBook
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun getMyBooks(): List<SavedBook>
    fun getPopularBooks(): Flow<List<Book>>
    fun getNewBooks(): Flow<List<Book>>
    fun getEBooks(): Flow<List<Book>>
    fun getBook(id: String?): Flow<Book?>
    fun getBookComments(id: String, page: Int): List<Comment>
    fun getBookByISBN(isbn: String): Flow<SavedBook?>
    fun searchBook(query: String): Flow<List<SearchResult>>
    fun saveBook(savedBook: SavedBook)
    fun removeBook(book: SavedBook)
}