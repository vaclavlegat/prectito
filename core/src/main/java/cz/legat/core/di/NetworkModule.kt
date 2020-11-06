package cz.legat.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    @Provides
    @Named("retrofit")
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            //.baseUrl("https://books-webapi.herokuapp.com/")
            .baseUrl("https://www.databazeknih.cz/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @Named("pdfRetrofit")
    fun providePdfRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://web2.mlp.cz/")
            //.addConverterFactory(ScalarsConverterFactory.create())
            .client(httpClient)
            .build()
    }
}