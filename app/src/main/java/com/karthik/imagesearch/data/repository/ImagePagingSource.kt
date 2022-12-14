package com.karthik.imagesearch.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.karthik.imagesearch.data.api.ImageSearchService
import com.karthik.imagesearch.data.model.Photo


class ImagePagingSource(private val apiService: ImageSearchService, private val keyWord: String) :
    PagingSource<Int, Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getImages(keyWord, position)

            LoadResult.Page(
                data = response.body()!!.photos!!.photo,
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}