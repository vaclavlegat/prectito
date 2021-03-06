package cz.legat.books.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksService {

    @GET("/")
    suspend fun getOverview(): Response<String>

    @GET("/knihy")
    suspend fun getPopularBooks(): Response<String>

    @GET("/prave-vyslo-knizni-novinky")
    suspend fun getNewBooks(): Response<String>

    @GET("/eknihy-zdarma-ke-stazeni")
    suspend fun getEBooks(): Response<String>

    @GET("knihy/{id}")
    suspend fun getBook(@Path("id") id: String, @Query("show") show: String = "binfo"): Response<String>

    @GET("/search")
    suspend fun getBookByISBN(@Query("q") isbn: String): Response<String>

    @GET("/search")
    suspend fun searchBook(@Query("q") query: String): Response<String>

    @GET("/knihy/{id}")
    suspend fun getBookComments(@Path("id") id: String, @Query("orderBy") orderBy: String = "new", @Query("str") page: Int): Response<String>

}