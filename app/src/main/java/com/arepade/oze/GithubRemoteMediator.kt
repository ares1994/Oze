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

        var loadKey = when (loadType) {
            LoadType.REFRESH -> 0
            // In this example, we never need to prepend, since REFRESH will always load the
            // first page in the list. Immediately return, reporting end of pagination.
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            // Query remoteKeyDao for the next RemoteKey.
            LoadType.APPEND -> {
                count = count!! +1

                count
            }
        }







//        Log.d("Ares", "state size is ${state.pages[0].data.size}")
//        Log.d("Ares", "state div value is ${(state.pages[0].data.size / 30) + 1}")
//        Log.d("Ares", "state is ${state.lastItemOrNull() == null}")
//
//        Log.d("Ares", "state is ${state.lastItemOrNull() == null}")
//        Log.d("Ares", "In Mediator")
//        Log.d("Ares", loadKey.toString())

        try {
            val response = service.getUsersAsync(page = loadKey.toString())


            val users = response.items


            try {
                viewModel.insert(users!!.map { it.apply { it?.page = count } })
            } catch (t: Throwable) {
                t.message?.let { Log.d("Ares", it) }
            }

//            val endOfPaginationReached = users!!.isEmpty()

            return MediatorResult.Success(
                endOfPaginationReached = false
            )

        } catch (t: Throwable) {
            return MediatorResult.Error(
                t
            )
        }
    }

}