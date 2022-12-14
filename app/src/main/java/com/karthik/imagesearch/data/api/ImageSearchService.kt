package com.karthik.imagesearch.data.api

import com.karthik.imagesearch.data.model.Imagelist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchService {

    @GET("?method=flickr.photos.search&api_key=96358825614a5d3b1a1c3fd87fca2b47&format=json&nojsoncallback=1")
    suspend fun getImages(
        @Query("text") keyword: String,
        @Query("page") page: Int
    ): Response<Imagelist>
}