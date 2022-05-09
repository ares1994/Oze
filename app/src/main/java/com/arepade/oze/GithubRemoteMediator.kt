package com.arepade.oze

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.UsersDao
import com.arepade.oze.database.UsersDatabase
import com.arepade.oze.network.Github

@ExperimentalPagingApi
class GithubRemoteMediator(private val service: Github, private val viewModel: MainViewModel) :
    RemoteMediator<Int, ItemsItem>() {
    private var count: Int? = null

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ItemsItem>
    ): MediatorResult {

        if (count == null) {
            count = if (state.lastItemOrNull() == null) {
                1
            } else {
                (state.pages[0].data.size / 30) + 1
            }
        }

        val loadKey = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                count = count!! + 1

                count
            }
        }


        try {
            val response = service.getUsersAsync(page = loadKey.toString())


            val users = response.items



            viewModel.insert(users!!.map { it.apply { it?.page = count } })



            return MediatorResult.Success(
                endOfPaginationReached = false
            )

        } catch (t: Throwable) {
            return MediatorResult.Error(
                t
            )
        }
    }

    companion object {
        const val TAG = "GithubRemoteMediator"
    }

}