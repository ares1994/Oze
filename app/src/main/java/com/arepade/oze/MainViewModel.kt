package com.arepade.oze

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.BookmarkedDao
import com.arepade.oze.database.UsersDao
import com.arepade.oze.network.Github
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(
    private val usersDao: UsersDao,
    private val bookmarkedDao: BookmarkedDao,
    private val service: Github
) : ViewModel() {


    fun insert(data: List<ItemsItem?>?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                usersDao.insert(*data!!.map { it!! }.toTypedArray())
            }
        }
    }

    fun insert(bookmarked: Bookmarked) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookmarkedDao.insert(bookmarked)
            }
        }
    }

    fun delete(bookmarked: Bookmarked) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookmarkedDao.delete(bookmarked)
            }
        }
    }

    fun clearAllBookmarks(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                bookmarkedDao.clear()
            }
        }
    }

    @ExperimentalPagingApi
    fun fetchPosts(): Flow<PagingData<ItemsItem>> {
        return Pager(
            config = PagingConfig(30),
            remoteMediator = GithubRemoteMediator(service, this)
        ) {
//            GithubRemotePagingSource(service, usersDao, this)
            usersDao.getAllUsers()

        }.flow.cachedIn(viewModelScope)
    }


}