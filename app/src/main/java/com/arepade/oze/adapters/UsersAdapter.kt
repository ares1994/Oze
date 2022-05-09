package com.arepade.oze.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arepade.oze.DetailsActivity
import com.arepade.oze.R
import com.arepade.oze.createSnackBar
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import com.arepade.oze.databinding.ItemListDataBinding
import com.arepade.oze.set
import com.squareup.picasso.Picasso

class UsersAdapter(
    diffCallback: DiffUtil.ItemCallback<ItemsItem>,
    private val onBookmarkListener: OnBookmarkListener
) :
    PagingDataAdapter<ItemsItem, UsersAdapter.UserViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), onBookmarkListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemListDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class UserViewHolder constructor(private val binding: ItemListDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            value: ItemsItem?,
            onBookmarkListener: OnBookmarkListener
        ) {
            val popupMenu = PopupMenu(binding.overflow.context, binding.overflow)

            popupMenu.inflate(R.menu.popup_menu)


            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.bookmark, R.id.remove -> {
                        onBookmarkListener.onDoAction(value)
                        createSnackBar(binding.root,"User Bookmarked")
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
                        putExtra(DATA, value)
                    }
                )
            }

            binding.overflow.setOnClickListener {
                popupMenu.show()
            }
        }


    }

    companion object {
        const val DATA = "Data"
    }

    interface OnBookmarkListener {
        fun onDoAction(value: ItemsItem?){}
        fun onDoAction(value: Bookmarked?){}
    }


}



