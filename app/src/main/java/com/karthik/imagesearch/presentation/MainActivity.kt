package com.karthik.imagesearch.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.karthik.imagesearch.data.ImagePagingAdapter
import com.karthik.imagesearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    private val adapter = ImagePagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.isVisible = false
        binding.txtPlaceholder.isVisible = true
        binding.rvImage.layoutManager = GridLayoutManager(this, 3)
        binding.rvImage.adapter = adapter

        binding.btnSearch.setOnClickListener {

            if (!binding.etSearch.text.isNullOrEmpty()) {
                lifecycleScope.launch {
                    viewModel.getAllSearchedImages(binding.etSearch.text.toString())
                        .observe(this@MainActivity) {
                            it?.let {

                                binding.txtPlaceholder.isVisible = false
                                adapter.submitData(lifecycle, it)
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Enter a valid text to search", Toast.LENGTH_SHORT).show()
            }
        }


        adapter.addLoadStateListener { loadState ->

            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) {
                    binding.progressBar.isVisible = false
                    binding.txtPlaceholder.isVisible = true
                }
                /// show empty view
                else {
                    binding.txtPlaceholder.isVisible = false
                }

            }

            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                binding.progressBar.isVisible = true
                binding.txtPlaceholder.isVisible = false
            } else {

                binding.progressBar.isVisible = false
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow
                .collectLatest {
                    if (it.refresh is LoadState.NotLoading) {
                        binding.progressBar.isVisible = false
                        binding.txtPlaceholder.isVisible = adapter.itemCount < 1
                    }
                }
        }

    }

}