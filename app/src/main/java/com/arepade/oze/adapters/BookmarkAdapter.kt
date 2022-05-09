package com.arepade.oze.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arepade.oze.DetailsActivity
import com.arepade.oze.R
import com.arepade.oze.createSnackBar
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.databinding.ItemListDataBinding
import com.arepade.oze.set
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class BookmarkAdapter(
    diffCallback: DiffUtil.ItemCallback<Bookmarked>,
    private val onBookmarkListener: UsersAdapter.OnBookmarkListener
) :
    ListAdapter<Bookmarked, BookmarkAdapter.BookmarkViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position), onBookmarkListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):BookmarkViewHolder {
        return BookmarkViewHolder(
            ItemListDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    class BookmarkViewHolder constructor(private val binding: ItemListDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            value: Bookmarked?,
            onBookmarkListener: UsersAdapter.OnBookmarkListener
        ) {
            val popupMenu = PopupMenu(binding.overflow.context, binding.overflow)

            popupMenu.inflate(R.menu.remove_menu)


            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.bookmark, R.id.remove -> {
                        onBookmarkListener.onDoAction(value)
                       createSnackBar(binding.root,"Bookmark Removed")
                    }
                }
                return@setOnMenuItemClickListener true
            }




            binding.nameTextView.text = value?.login
            Picasso.get().set(binding.avatarImageView, value?.avatarUrl)


            binding.root.setOnClickListener {
                binding.root.context.startActivity(
                    Intent(
                        binding.root.context,
                        DetailsActivity::class.java
                    ).apply {
                        putExtra(UsersAdapter.DATA, value?.run {
                            ItemsItem(
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
                                page
                            )
                        })
                    }
                )
            }


            binding.overflow.setOnClickListener {
                popupMenu.show()
            }
        }


    }


}
