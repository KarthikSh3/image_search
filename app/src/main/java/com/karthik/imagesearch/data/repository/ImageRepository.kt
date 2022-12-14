package com.karthik.imagesearch.data.repository

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.karthik.imagesearch.data.api.ImageSearchService
import com.karthik.imagesearch.data.model.Photo
import javax.inject.Inject

class ImageRepository @Inject constructor(private val imageSearchService: ImageSearchService) {
    private val initialLoadSize: Int = 2
    private val initialKey: Int = 1

    fun getAllImages(keyWord: String): LiveData<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = initialLoadSize
            ), pagingSourceFactory = {
                ImagePagingSource(imageSearchService, keyWord)
            }, initialKey = initialKey
        ).liveData
    }
}