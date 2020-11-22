package cz.legat.core.repository

import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.core.model.Overview
import cz.legat.core.persistence.SavedBook

interface BooksRepository {
    suspend fun getOverview(): Overview
    suspend fun getMyBooks(): List<SavedBook>
    suspend fun getPopularBooks(): List<Book>
    suspend fun getNewBooks(): List<Book>
    suspend fun getEBooks(): List<Book>
    suspend fun getBook(id: String?): Book?
    fun getBookComments(id: String?): List<Comment>
    fun getBookCommentsByPage(page:Int, param: String?): List<Comment>
    suspend fun getBookByISBN(isbn: String): SavedBook?
    suspend fun searchBook(query: String): List<Book>
    fun saveBook(savedBook: SavedBook)
    fun removeBook(book: SavedBook)
}