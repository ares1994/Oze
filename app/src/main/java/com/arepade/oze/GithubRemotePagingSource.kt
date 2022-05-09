package com.arepade.oze

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.UsersDao
import com.arepade.oze.network.Github
import dagger.hilt.android.AndroidEntryPoint


class GithubRemotePagingSource constructor(
    private val service: Github,
    private val userDao: UsersDao,
    private val viewModel: MainViewModel
) :
    PagingSource<Int, ItemsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ItemsItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsItem> {

        val l = mutableListOf<ItemsItem>()

        var p = 1




//        if (params.key == null) {
//            val list = viewModel.data.value
//
//            if (!list.isNullOrEmpty()) {
//                p = list.size / 30
//                l.addAll(list)
//            }
//
//            Log.d("Ares", "In PagingSource ${list.toString()}")
//        }


        return try {
            val page = params.key ?: p
            val response = service.getUsersAsync(page = page.toString())

            try {
                viewModel.insert(response.items!!.map { it.apply { it?.page = page } })
            }catch (t:Throwable){
                t.message?.let { Log.d("Ares", it) }
            }


            l.addAll(response.items!!.map { it!! })


            LoadResult.Page(
                data = l,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (t: Throwable) {
            Log.d("Ares", "In PagingSource-> ${t.message}")
            LoadResult.Error(t)
        }

    }


}