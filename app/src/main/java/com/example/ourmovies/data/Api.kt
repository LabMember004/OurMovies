package com.example.ourmovies.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("genre") genre: String? = null,
        @Query("releaseYear") releaseYear: Int? = null,
        @Query("query") query: String? = null
    ): MoviesResponse

    @GET("movies/sections")
    suspend fun getSections(): SectionsResponse

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
    @PATCH("me")
    suspend fun updateEmail(
        @Header("Authorization") token: String,
        @Body request : UpdateEmailRequest
    ): Response<UpdateEmailResponse>

    @PATCH("me/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body request: UpdatePasswordRequest
    ): Response<UpdatePasswordResponse>

    @DELETE("me")
    suspend fun deleteProfile(
        @Header("Authorization") token: String
    ): Response<Unit>
}