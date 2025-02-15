package com.example.ourmovies.domain

import android.media.session.MediaSession.Token
import com.example.ourmovies.data.FavoriteRequest
import com.example.ourmovies.data.LoginRequest
import com.example.ourmovies.data.LoginResponse
import com.example.ourmovies.data.Movies
import com.example.ourmovies.data.MoviesResponse
import com.example.ourmovies.data.RegisterRequest
import com.example.ourmovies.data.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int
    ): MoviesResponse

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("favorites")
    suspend fun addToFavorites (
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    ): Response<ResponseBody>

    @GET("favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): Response<List<Movies>>

    @DELETE("favorites/{favoriteId}")
    suspend fun deleteFavorite(
        @Path("favoriteId") favoriteId: String,
        @Header("Authorization") token: String
    ): Response<ResponseBody>

}