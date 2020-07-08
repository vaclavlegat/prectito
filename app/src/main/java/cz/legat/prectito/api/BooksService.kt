package cz.legat.prectito.api

import cz.legat.prectito.model.Book
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksService {

    @GET("/books/popular")
    suspend fun getPopularBooks(): List<Book>

    @GET("/books/new")
    suspend fun getNewBooks(): List<Book>

    @GET("/books/detail/{id}")
    suspend fun getBook(@Path("id") id: String): Book

}