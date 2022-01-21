package com.asifddlks.imagesearchapp.apiclient

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiClient {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"

        //const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY

        //this may not include in git. so i put it hardcoded here.
        const val CLIENT_ID = "Lpsl46_cTsqZzBpAPlvy1v7bCCGcGqC-1Xnj8h1e6oE"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ApiResponse
}