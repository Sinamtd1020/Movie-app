package com.sinamtd.movieapp.di

import com.sinamtd.movieapp.data.remote.api.MovieApi
import com.sinamtd.movieapp.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL="https://api.themoviedb.org/3/"




    @Singleton
    @Provides
    fun provideAuthInterceptor(): AuthInterceptor{
        return AuthInterceptor("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTc3YmUzODc3MzgxYWEwYjA2M2VlMjE3NDM5ZDk3NiIsIm5iZiI6MTc4NjIwMzA1My4xODU5OTk5LCJzdWIiOiI2YTc3NGJhZDYzODU4OWRhZjkyNjNlYjQiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.u3OlFHc7zHINCJw7jhn5F2GNZhjYpu8N1U6Zj1BBsLk")
    }

    @Singleton
    @Provides
    fun provideOkHttp(authInterceptor: AuthInterceptor): OkHttpClient {
       return OkHttpClient.Builder()
           .addInterceptor(authInterceptor)
           .build()
    }
    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit=
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi{
        return retrofit.create(MovieApi::class.java)
    }
}