package com.arepade.oze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.arepade.oze.adapters.UsersAdapter
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val data = intent.getParcelableExtra<ItemsItem>(UsersAdapter.DATA)

        data?.let {
            Picasso.get().set(binding.imageView, it.avatarUrl)

            binding.usernameTextView.text = it.login
            binding.scoreTextView.text = it.score.toString()

        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Details"
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}