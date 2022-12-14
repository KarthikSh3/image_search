package com.karthik.imagesearch.data.model

import com.google.gson.annotations.SerializedName


data class Imagelist(

    @SerializedName("photos") var photos: Photos? = Photos(),

    )