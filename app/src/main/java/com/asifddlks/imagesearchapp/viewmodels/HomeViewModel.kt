package com.asifddlks.imagesearchapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.asifddlks.imagesearchapp.apiclient.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "time"
    }
}