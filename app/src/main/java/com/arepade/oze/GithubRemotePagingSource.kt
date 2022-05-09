package com.arepade.oze

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.UsersDao
import com.arepade.oze.network.Github
import dagger.hilt.android.AndroidEntryPoint


class GithubRemotePagingSource constructor(
    private val service: Github
) :
    PagingSource<Int, ItemsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ItemsItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsItem> {


        return try {
            val page = params.key ?: 1
            val response = service.getUsersAsync(page = page.toString())




            LoadResult.Page(
                data = response.items!!.map { it!! },
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (t: Throwable) {
            Log.d(TAG, "In PagingSource-> ${t.message}")
            LoadResult.Error(t)
        }

    }


    companion object {
        const val TAG = "GithubRemotePaging"
    }
}