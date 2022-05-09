package com.arepade.oze

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

fun Picasso.set(view: ImageView, url: String?) {
    try {
        Picasso.get().load(url).into(view, object : Callback {
            override fun onSuccess() {

            }

            override fun onError(e: Exception?) {
                view.setImageResource(R.drawable.dummy_image)
            }
        })
    } catch (e: Exception) {
        view.setImageResource(R.drawable.dummy_image)
    }
}


val ItemDiffUtil = object : DiffUtil.ItemCallback<ItemsItem>() {
    override fun areItemsTheSame(
        oldItem: ItemsItem,
        newItem: ItemsItem
    ): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(
        oldItem: ItemsItem,
        newItem: ItemsItem
    ): Boolean {
        return oldItem == newItem
    }
}


val BookmarkDiffUtil = object : DiffUtil.ItemCallback<Bookmarked>() {
    override fun areItemsTheSame(
        oldItem: Bookmarked,
        newItem: Bookmarked
    ): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(
        oldItem: Bookmarked,
        newItem: Bookmarked
    ): Boolean {
        return oldItem == newItem
    }
}


fun createSnackBar(view: View, data: String) =
    Snackbar.make(view, data, Snackbar.LENGTH_LONG).show()
