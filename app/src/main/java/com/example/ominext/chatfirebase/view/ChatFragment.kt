package com.example.ominext.chatfirebase.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.ChatAdapter
import com.example.ominext.chatfirebase.model.LoadingItem
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.Status
import com.example.ominext.chatfirebase.presenter.ChatPresenter
import com.example.ominext.chatfirebase.util.Utils
import java.util.*

/**
 * Created by Ominext on 8/1/2017.
 */

class ChatFragment : Fragment() {
    @BindView(R.id.recyclerview_detailchat)
    lateinit var rvChat: RecyclerView

    @BindView(R.id.edittext_message)
    lateinit var etMessage: EditText

    @BindView(R.id.imageview_send)
    lateinit var imvSend: ImageView

    @BindView(R.id.textview_name)
    lateinit var tvName: TextView

    @BindView(R.id.textview_status)
    lateinit var tvStatus: TextView

    @BindView(R.id.progressbar_loading)
    lateinit var pbLoading: ProgressBar

    @BindView(R.id.toolbar_detail_chat)
    lateinit var toolbar: Toolbar

    val mPresenter: ChatPresenter = ChatPresenter()
    lateinit var mAdapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)

        mPresenter.addView(this)
        mPresenter.getData(arguments)

        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        tvName.text = mPresenter.userFriend?.name ?: ""
        tvStatus.text = if (mPresenter.userFriend?.status == Status.ONLINE.name) {
            "Đang hoạt động"
        } else {
            Utils.getTimeAgoUser(context, mPresenter.userFriend?.lastOnline!!)
        }

        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        rvChat.layoutManager = layoutManager
        rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy >= 0) return

                if (layoutManager.findFirstVisibleItemPosition() - 1 <= 0) {
                    mPresenter.onLoadMessage()
                }
            }
        })
        mAdapter = ChatAdapter(mPresenter.listMessage, mPresenter.currentUser, mPresenter.userFriend, {
            mPresenter.onLoadMessage()
        })
        rvChat.adapter = mAdapter

        etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (etMessage.text.toString().trim().isEmpty()) {
                    imvSend.drawable.level = 1
                } else {
                    imvSend.drawable.level = 2
                }
            }
        })

        mPresenter.onLoadMessage()
    }

    @OnClick(R.id.imageview_send)
    fun onClickSend() {
        mPresenter.sendMessage(context, etMessage.text.toString(), imvSend.drawable.level)
    }

    fun insertMessage(message: Message?) {
        mAdapter.add(message = message)
        etMessage.text.clear()
    }

    fun showProgressBar(b: Boolean) {
        pbLoading.isEnabled = b
        if (b) {
            pbLoading.visibility = View.VISIBLE
        } else {
            pbLoading.visibility = View.GONE
        }
    }

    fun addLoadingType() {
        val size = mPresenter.listMessage.size
        if (size > 0 && mPresenter.listMessage.first() !is LoadingItem) {
            mPresenter.listMessage.add(0, LoadingItem())
            rvChat.post {
                mAdapter.notifyItemInserted(0)
            }
        }
    }

    fun removeLoadingItem(position: Int) {
        mAdapter.removeItem(position)
    }

    fun addMessage(messages: ArrayList<Message>, position: Int, page: Int) {
        mAdapter.addAll(messages, position)
        if (page == 1 && mPresenter.listMessage.isNotEmpty()) {
            rvChat.scrollToPosition(mPresenter.listMessage.size - 1)
        }
    }

    fun scrollToBottom() {
        rvChat.scrollToPosition(mPresenter.listMessage.size - 1)
    }

    fun updateStatusMessage(oldMessage: Message, newIdMessage: String?, newCreatedAt: Long) {
        mAdapter.updateMessage(oldMessage, newIdMessage, newCreatedAt)
        rvChat.scrollToPosition(mPresenter.listMessage.size - 1)
    }
}