package com.karthik.imagesearch.presentation

import androidx.lifecycle.liveData
import com.karthik.imagesearch.data.repository.ImageRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Test


class MainViewModelShould {

    private lateinit var viewModel: MainViewModel
    private val repository: ImageRepository = mockk()
    private var testKeyword: String = "Lion"

    @Test
    fun getAllSearchedImagesFromRepo() {
        viewModel = MainViewModel(repository)

        every {
            repository.getAllImages(testKeyword)
        } returns liveData {
        }

        viewModel.getAllSearchedImages(testKeyword)

        coVerify { repository.getAllImages(testKeyword) }
    }

}