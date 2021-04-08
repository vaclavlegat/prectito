package cz.legat.authors.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthorsService {

    @GET("/autori")
    suspend fun getAuthors(
        @Query("pageNumber") pageNumber: Int,
        @Query("orderBy") by: String = "rat",
        @Query("nationId") nid: String?
    ): Response<String>

    @GET("/zivotopis/{id}")
    suspend fun getAuthor(@Path("id") id: String): Response<String>

    @GET("/search")
    suspend fun searchAuthor(
        @Query("q") query: String,
        @Query("in") where: String = "authors"
    ): Response<String>

    @GET("vydane-knihy/{authorId}")
    suspend fun getAuthorBooks(
        @Path("authorId") authorId: String,
        @Query("odb") sort: String?,
        @Query("id") page: Int
    ): Response<String>
}