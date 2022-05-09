package com.arepade.oze

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.arepade.oze.adapters.BookmarkAdapter
import com.arepade.oze.adapters.UsersAdapter
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.database.BookmarkedDao
import com.arepade.oze.databinding.ActivityBookmarkedBinding
import com.arepade.oze.databinding.ActivityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


@AndroidEntryPoint
class BookmarkedActivity : AppCompatActivity(), UsersAdapter.OnBookmarkListener {
    private lateinit var binding: ActivityBookmarkedBinding
    private val viewModel by viewModels<MainViewModel>()


    @Inject
    lateinit var bookmarkedDao: BookmarkedDao

    private val mAdapter = BookmarkAdapter(BookmarkDiffUtil, this)

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookmarkedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = mAdapter

        bookmarkedDao.getBookmarked().observeOn(AndroidSchedulers.mainThread()).subscribe {
            mAdapter.submitList(it)
        }


        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Bookmarks"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.clearAll -> {
                viewModel.clearAllBookmarks()
                createSnackBar(binding.root, "Bookmarks Cleared")
            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDoAction(value: Bookmarked?) {
        value?.let { viewModel.delete(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.bookmark_menu, menu)
        return true
    }

}