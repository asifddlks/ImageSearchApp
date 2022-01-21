package com.asifddlks.imagesearchapp.apiclient

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.asifddlks.imagesearchapp.model.ImageModel
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class PagingSource(
    private val unsplashApi: ApiClient,
    private val query: String
) : PagingSource<Int, ImageModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageModel>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


}