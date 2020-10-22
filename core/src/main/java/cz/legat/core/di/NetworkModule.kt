package cz.legat.core.di

import cz.legat.core.api.BooksService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideBookService(retrofit: Retrofit): BooksService {
        return retrofit.create(BooksService::class.java)
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            //.baseUrl("https://books-webapi.herokuapp.com/")
            .baseUrl("https://www.databazeknih.cz/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(httpClient)
            .build()
    }
}