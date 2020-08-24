package cz.legat.prectito.api

import cz.legat.prectito.model.Book
import cz.legat.prectito.model.google.VolumeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksService {

    @GET("books/v1/volumes")
    suspend fun getBookByISBN(@Query("q") query:String): Response<VolumeResponse>

}