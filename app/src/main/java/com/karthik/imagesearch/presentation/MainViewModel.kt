package com.karthik.imagesearch.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.karthik.imagesearch.data.model.Photo
import com.karthik.imagesearch.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {


    fun getAllSearchedImages(keyword: String): LiveData<PagingData<Photo>> {
        return imageRepository.getAllImages(keyword).cachedIn(viewModelScope)
    }
}