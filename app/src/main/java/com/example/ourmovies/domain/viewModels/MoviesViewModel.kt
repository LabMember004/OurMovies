package com.example.ourmovies.domain.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ourmovies.data.FavoriteRequest
import com.example.ourmovies.data.Movies
import com.example.ourmovies.data.MoviesResponse
import com.example.ourmovies.data.Section
import com.example.ourmovies.data.SectionsResponse
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

    private val _sections = MutableStateFlow<List<Section>>(emptyList())
    val sections: StateFlow<List<Section>> = _sections.asStateFlow()

    private val _highlightedMovie = MutableStateFlow<Movies?>(null)
    val highlightedMovie: StateFlow<Movies?> = _highlightedMovie.asStateFlow()

    private var currentPage = 1
    var isLoading by mutableStateOf(false)
    var isEndReached by mutableStateOf(false)

    init {
        getSections()
        fetchData(currentPage)
    }

    private fun fetchData(page: Int, genre: String? = null, releaseYear: Int? = null, query: String? = null) {
        if (isLoading || isEndReached) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = repository.getMovieList(page, genre, releaseYear, query)
                if (response.data.isNotEmpty()) {
                    _data.value += response.data
                    currentPage = page
                    isEndReached = currentPage >= response.totalPages
                }
                _paginationData.value = response
            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Error fetching movies: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun applyFilters(genre: String?, releaseYear: Int?, query: String?) {
        currentPage = 1
        isEndReached = false
        _data.value = emptyList()
        fetchData(currentPage, query = query, genre = genre, releaseYear = releaseYear)
    }

    fun loadNextPage() {
        if (!isEndReached) {
            fetchData(currentPage + 1)
        }
    }

    fun getSections() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getSections()
                _sections.value = response.sections

                // Set the first movie from the first section as highlighted (if available)
                val firstMovie = response.sections.firstOrNull()?.movies?.firstOrNull()
                if (firstMovie != null) {
                    _highlightedMovie.value = firstMovie
                }

                Log.d("MoviesViewModel", "Sections fetched: ${response.sections}")
            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Error fetching sections: ${e.message}")
            }
        }
    }

}









