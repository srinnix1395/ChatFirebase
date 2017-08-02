package com.example.ominext.chatfirebase.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.viewholder.UserViewHolder
import com.example.ominext.chatfirebase.model.User

/**
 * Created by Ominext on 8/2/2017.
 */
class UsersAdapter(val list: ArrayList<User>,
                   val onClickItem: (position: Int) -> Unit) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        holder?.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, onClickItem)
    }

    fun addAll(index: Int = list.size, values: MutableCollection<User>) {
        list.addAll(values)
        notifyItemRangeInserted(index, values.size)
    }
}