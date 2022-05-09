package com.arepade.oze

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.arepade.oze.adapters.UsersAdapter
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.BookmarkedDao
import com.arepade.oze.database.UsersDao
import com.arepade.oze.databinding.ActivityMainBinding
import com.arepade.oze.network.Github
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UsersAdapter.OnBookmarkListener {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var github: Github

    @Inject
    lateinit var userDao: UsersDao

    @Inject
    lateinit var bookmarkedDao: BookmarkedDao

    private lateinit var binding: ActivityMainBinding


    private val nAdapter = UsersAdapter(ItemDiffUtil, this)


    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



        binding.recyclerView.adapter = nAdapter


        lifecycleScope.launchWhenResumed {
            viewModel.fetchPosts().collectLatest {
                nAdapter.submitData(it)
            }
        }



//        binding.print.setOnClickListener {
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    Log.d("Ares", bookmarkedDao.getBookmarked().value.toString())
//                }
//            }
//        }


    }

    override fun onDoAction(value: ItemsItem?) {

        value?.apply {
            viewModel.insert(
                Bookmarked(
                    gistsUrl,
                    reposUrl,
                    followingUrl,
                    starredUrl,
                    login,
                    followersUrl,
                    type,
                    url,
                    subscriptionsUrl,
                    score,
                    receivedEventsUrl,
                    avatarUrl,
                    eventsUrl,
                    htmlUrl,
                    siteAdmin,
                    id,
                    gravatarId,
                    nodeId,
                    organizationsUrl,
                    page,
                    true
                )
            )
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.showBookmarked -> {
                startActivity(Intent(this, BookmarkedActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}