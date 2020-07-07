package cz.legat.prectito.di

import cz.legat.prectito.api.BooksService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
    return  Retrofit.Builder()
      .baseUrl("https://books-webapi.herokuapp.com/")
      .addConverterFactory(MoshiConverterFactory.create())
      .client(httpClient)
      .build()
  }
}