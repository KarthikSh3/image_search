package com.karthik.imagesearch.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.karthik.imagesearch.data.api.ImageSearchService
import com.karthik.imagesearch.data.model.Imagelist
import com.karthik.imagesearch.data.model.Photo
import com.karthik.imagesearch.data.model.Photos
import com.karthik.imagesearch.data.repository.ImagePagingSource
import com.karthik.imagesearch.utils.MainCoroutineScopeRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response.success

class PagingSourceShould {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = MainCoroutineScopeRule()

    val api: ImageSearchService = mockk()

    lateinit var pagingSource: ImagePagingSource

    companion object {
        val photos1: Photos = Photos(1, 10, 20, 100, arrayListOf(Photo(title = "Lion")))
        val photos2: Photos = Photos(2, 10, 20, 100, arrayListOf(Photo(title = "Lion")))
        val responseList = Imagelist(photos1)
        val nextResponseList = Imagelist(photos2)
    }

    @Before
    fun setUp() {
        pagingSource = ImagePagingSource(api, "Lion")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `returns failure for http error`() = runTest {
        val error = RuntimeException("404", Throwable())

        coEvery { api.getImages("Lion", 0) } throws error
        val expectedResult = PagingSource.LoadResult.Error<Int, Photo>(error)

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `returns failure for null response`() = runTest {
        coEvery { api.getImages("Lion", 0) } returns success(null)

        val expectedResult =
            PagingSource.LoadResult.Error<Int, Photo>(java.lang.NullPointerException())

        assertEquals(
            expectedResult.toString(), pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ).toString()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `returns success for refresh`() = runTest {
        coEvery { api.getImages("Lion", 0) } returns success(responseList)

        val expectedResult =
            PagingSource.LoadResult.Page<Int, Photo>(data = responseList.photos!!.photo, -1, 1)

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `returns success for append`() = runTest {
        coEvery { api.getImages("Lion", 0) } returns success(nextResponseList)

        val expectedResult = PagingSource.LoadResult.Page(data = responseList.photos!!.photo, -1, 1)

        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `returns success for prepend`() = runTest {
        coEvery { api.getImages("Lion", 0) } returns success(responseList)
        val expectedResult = PagingSource.LoadResult.Page(data = responseList.photos!!.photo, -1, 1)
        assertEquals(
            expectedResult, pagingSource.load(
                PagingSource.LoadParams.Prepend(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }
}