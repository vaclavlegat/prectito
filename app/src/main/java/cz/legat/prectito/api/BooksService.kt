package cz.legat.prectito.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksService {

    @GET("/knihy")
    suspend fun getPopularBooks(): Response<String>

    @GET("/prave-vyslo-knizni-novinky")
    suspend fun getNewBooks(): Response<String>

    @GET("knihy/{id}")
    suspend fun getBook(@Path("id") id: String, @Query("show") show: String = "binfo"): Response<String>

    @GET("/books/isbn")
    suspend fun getBookByISBN(@Query("q") isbn: String): Response<String>

    @GET("/search")
    suspend fun searchBook(@Query("q") query: String): Response<String>

    @GET("/knihy/{id}")
    suspend fun getBookComments(@Path("id") id: String, @Query("c") by: String = "all"): Response<String>

    @GET("/autori")
    suspend fun getAuthors(@Query("page") page: Int, @Query("dle") by: String = "rat"): Response<String>

    @GET("/zivotopis/{id}")
    suspend fun getAuthor(@Path("id") id: String): Response<String>

    @GET("/search")
    suspend fun searchAuthor(@Query("q") query: String, @Query("in") where: String = "authors"): Response<String>

    @GET("vydane-knihy/{authorId}")
    suspend fun getAuthorBooks(@Path("authorId") authorId: String, @Query("odb") sort: String?, @Query("id") page: Int): Response<String>


    /*@GET("/books/popular")
    suspend fun getPopularBooks(): Response<List<Book>>

    @GET("/books/new")
    suspend fun getNewBooks(): Response<List<Book>>

    @GET("/books/detail/{id}")
    suspend fun getBook(@Path("id") id: String): Response<Book>

    @GET("/books/isbn")
    suspend fun getBookByISBN(@Query("q") isbn: String): Response<Book>

    @GET("/books/search")
    suspend fun searchBook(@Query("q") query: String): Response<List<Book>>

    @GET("/books/detail/{id}/comments")
    suspend fun getBookComments(@Path("id") id: String): Response<List<Comment>>

    @GET("/authors")
    suspend fun getAuthors(@Query("page") page: Int): Response<List<Author>>

    @GET("/authors/{id}")
    suspend fun getAuthor(@Path("id") id: String): Response<Author>

    @GET("/authors/search")
    suspend fun searchAuthor(@Query("q") query: String): Response<List<Author>>*/
}