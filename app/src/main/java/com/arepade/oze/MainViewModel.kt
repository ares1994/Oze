package com.arepade.oze

import android.annotation.SuppressLint
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.observable
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.BookmarkedDao
import com.arepade.oze.database.UsersDao
import com.arepade.oze.network.Github
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class MainViewModel @ViewModelInject constructor(
    private val usersDao: UsersDao,
    private val bookmarkedDao: BookmarkedDao,
    private val service: Github
) : ViewModel() {

    private val disposable = CompositeDisposable()

    fun insert(data: List<ItemsItem?>?) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                usersDao.insert(*data!!.map { it!! }.toTypedArray())
//            }
//        }

        disposable.add(
            usersDao.insert(*data!!.map { it!! }.toTypedArray()).subscribeOn(Schedulers.io())
                .subscribe()
        )
    }


    fun insert(bookmarked: Bookmarked) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                bookmarkedDao.insert(bookmarked)
//            }
//        }

        disposable.add(
            bookmarkedDao.insert(bookmarked).subscribeOn(Schedulers.io()).subscribe()
        )
    }

    fun delete(bookmarked: Bookmarked) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                bookmarkedDao.delete(bookmarked)
//            }
//        }
        disposable.add(
            bookmarkedDao.delete(bookmarked).subscribeOn(Schedulers.io()).subscribe()
        )
    }

    fun clearAllBookmarks() {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                bookmarkedDao.clear()
//            }
//        }


        Observable.create<Unit> {
            it.onNext(bookmarkedDao.clear())
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    fun fetchPosts(): Observable<PagingData<ItemsItem>> {
        return Pager(
            config = PagingConfig(30),
            remoteMediator = GithubRemoteMediator(service, this)
        ) {
//            GithubRemotePagingSource(service, usersDao, this)
            usersDao.getAllUsers()

        }.observable.cache()
    }


}