package cz.legat.prectito.api

import cz.legat.prectito.model.Book
import retrofit2.http.GET

interface BooksService {

    @GET("/books/popular")
    suspend fun getPopularBooks(): List<Book>

    @GET("/books/new")
    suspend fun getNewBooks(): List<Book>

}