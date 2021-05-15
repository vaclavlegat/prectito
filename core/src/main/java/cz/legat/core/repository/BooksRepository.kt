package cz.legat.core.repository

import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.core.model.Overview
import cz.legat.core.persistence.SavedBook
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun getMyBooks(): List<SavedBook>
    fun getPopularBooks(): Flow<List<Book>>
    fun getNewBooks(): Flow<List<Book>>
    fun getEBooks(): Flow<List<Book>>
    suspend fun getBook(id: String?): Book?
    fun getBookComments(id: String, page: Int): List<Comment>
    suspend fun getBookByISBN(isbn: String): SavedBook?
    suspend fun searchBook(query: String): List<Book>
    fun saveBook(savedBook: SavedBook)
    fun removeBook(book: SavedBook)
}