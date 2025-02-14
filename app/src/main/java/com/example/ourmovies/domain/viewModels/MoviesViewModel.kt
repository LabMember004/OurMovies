package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.FavoriteRequest
import com.example.ourmovies.data.Movies
import com.example.ourmovies.data.MoviesResponse
import com.example.ourmovies.domain.Repository
import com.example.ourmovies.domain.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel: ViewModel() {
    private val repository = Repository()

    private val _paginationData = MutableStateFlow<MoviesResponse?>(null)
    private val _data = MutableStateFlow<List<Movies>>(emptyList())
    val data: StateFlow<List<Movies>> = _data.asStateFlow()




    private var currentPage = 1
    var isLoading by mutableStateOf(false)
    var isEndReached by mutableStateOf(false)



    init {
        fetchData(currentPage)
    }

    private fun fetchData(page: Int) {
        if (isLoading || isEndReached) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = repository.getMovieList(page)
                if (response.data.isNotEmpty()) {
                    _data.value += response.data
                    currentPage = page
                    isEndReached = currentPage >= response.totalPages
                }
                _paginationData.value = response
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun loadNextPage() {
        if (!isEndReached) {
            fetchData(currentPage + 1)
        }
    }

}








