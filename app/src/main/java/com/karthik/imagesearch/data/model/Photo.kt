package com.karthik.imagesearch.data.model

import com.google.gson.annotations.SerializedName


data class Photo(

    @SerializedName("id") var id: String? = null,
    @SerializedName("secret") var secret: String? = null,
    @SerializedName("server") var server: String? = null,
    @SerializedName("farm") var farm: Int? = null,
    @SerializedName("title") var title: String? = null,

    ) {
    fun getImageUrl(): String {
        return "http://farm$farm.static.flickr.com/$server/${id}_$secret.jpg"
    }
}