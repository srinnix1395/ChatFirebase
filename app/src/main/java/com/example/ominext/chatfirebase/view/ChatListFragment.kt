package com.example.ominext.chatfirebase.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.UsersAdapter
import com.example.ominext.chatfirebase.adapter.payload.StatusPayload
import com.example.ominext.chatfirebase.model.User
import com.example.ominext.chatfirebase.presenter.ChatListPresenter

/**
 * Created by Ominext on 8/2/2017.
 */
class ChatListFragment : Fragment() {
    @BindView(R.id.rvUser)
    lateinit var rvUser: RecyclerView

    @BindView(R.id.progressbar_loading)
    lateinit var pbLoading: ProgressBar

    lateinit var mPresenter: ChatListPresenter
    lateinit var mAdapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)

        mPresenter = ChatListPresenter()
        mPresenter.addView(this)

        mAdapter = UsersAdapter(mPresenter.listUser, { position ->
            mPresenter.onClickItem(context,position)
        })
        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(context)
        rvUser.adapter = mAdapter

        mPresenter.getUsers()
    }

    fun insertUser(values: MutableCollection<User>) {
        mAdapter.addAll(values = values)
    }

    fun disableProgressbar() {
        pbLoading.isEnabled = false
        pbLoading.visibility = View.GONE
    }

    fun updateStatus(index: Int, status: String) {
        mAdapter.notifyItemChanged(index, StatusPayload(status))
    }
}