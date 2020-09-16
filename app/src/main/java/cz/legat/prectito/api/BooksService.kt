package cz.legat.prectito.api

import cz.legat.prectito.model.Author
import cz.legat.prectito.model.Book
import cz.legat.prectito.model.Comment
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksService {

    @GET("/books/popular")
    suspend fun getPopularBooks(): Response<List<Book>>

    @GET("/books/new")
    suspend fun getNewBooks(): Response<List<Book>>

    @GET("/books/detail/{id}")
    suspend fun getBook(@Path("id") id: String): Book

    @GET("/books/isbn")
    suspend fun getBookByISBN(@Query("q") isbn: String): Book

    @GET("/books/search")
    suspend fun searchBook(@Query("q") query: String): List<Book>

    @GET("/books/detail/{id}/comments")
    suspend fun getBookComments(@Path("id") id: String): List<Comment>

    @GET("/authors")
    suspend fun getAuthors(@Query("page") page: Int): Response<List<Author>>

    @GET("/authors/{id}")
    suspend fun getAuthor(@Path("id") id: String): Response<Author>

    @GET("/authors/search")
    suspend fun searchAuthor(@Query("q") query: String): List<Author>
}