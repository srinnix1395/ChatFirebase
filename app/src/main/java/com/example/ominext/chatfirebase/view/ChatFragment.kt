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
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.LoadingItem
import com.example.ominext.chatfirebase.model.Message
import com.example.ominext.chatfirebase.model.Status
import com.example.ominext.chatfirebase.model.TypeMessage
import com.example.ominext.chatfirebase.presenter.ChatPresenter
import com.example.ominext.chatfirebase.util.Utils

/**
 * Created by Ominext on 8/1/2017.
 */

class ChatFragment : Fragment() {
    @BindView(R.id.recyclerview_detailchat)
    lateinit var rvChat: RecyclerView

    @BindView(R.id.edittext_message)
    lateinit var etMessage: EditText

    @BindView(R.id.imagebutton_send)
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
        setStatus(mPresenter.userFriend?.status)

        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        rvChat.layoutManager = layoutManager
        rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy >= 0) return

                if (layoutManager.findFirstCompletelyVisibleItemPosition() - 1 <= 0) {
                    mPresenter.onLoadMessage()
                }
            }
        })
        rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom && !mPresenter.listMessage.isEmpty()) {
                rvChat.postDelayed({ rvChat.smoothScrollToPosition(mPresenter.listMessage.size - 1) }, 100)
            }
        }
        mAdapter = ChatAdapter(mPresenter.listMessage, mPresenter.currentUser, mPresenter.userFriend, {
            mPresenter.onLoadMessage()
        })
        rvChat.adapter = mAdapter

        etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                mPresenter.onUserTyping(p0.toString())
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

    fun setStatus(status: String?) {
        tvStatus.text = if (status == Status.ONLINE.name) {
            "Đang hoạt động"
        } else {
            Utils.getTimeAgoUser(context, mPresenter.userFriend?.lastOnline!!)
        }
    }

    @OnClick(R.id.imagebutton_send)
    fun onClickSend() {
        mPresenter.sendMessage(context, etMessage.text.toString(), imvSend.drawable.level)
    }

    @OnClick(R.id.imagebutton_image)
    fun onClickSelectImage() {
        val bundle = Bundle()
        bundle.putParcelableArrayList(ChatConstant.KEY_MEDIA, null)
        bundle.putInt(ChatConstant.KEY_MEDIA_TYPE, ChatConstant.TYPE_IMAGE)
        bundle.putInt(ChatConstant.KEY_LIMIT, 1)

        //todo show image picker fragment
    }

    fun showProgressBar(b: Boolean) {
        pbLoading.isEnabled = b
        if (b) {
            pbLoading.visibility = View.VISIBLE
        } else {
            pbLoading.visibility = View.GONE
        }
    }

    fun addLoadingItem() {
        val size = mPresenter.listMessage.size
        if (size > 0 && mPresenter.listMessage.first() !is LoadingItem) {
            mPresenter.listMessage.add(0, LoadingItem())
        }
    }

    fun removeLoadingItem(position: Int) {
        mAdapter.removeItem(position)
    }

    fun addAll(messages: ArrayList<Any>, position: Int, page: Int) {
        mAdapter.addAll(messages, position)
        if (page == 1) {
            scrollToBottom()
        }
    }

    fun add(index: Int, message: Message?) {
        mAdapter.add(index, message)
        etMessage.text.clear()
        scrollToBottom()
    }

    fun updateStatusMessage(idMessage: String?, createdAt: Long?) {
        mAdapter.updateMessage(idMessage, createdAt)
        scrollToBottom()
    }

    fun removeItem(position: Int) {
        mAdapter.removeItem(position)
    }

    fun showTypingMessage(isFriendTyping: Boolean) {
        if (isFriendTyping) {
            val messageTyping = Message()
            messageTyping.messageType = TypeMessage.TYPING.name

            mAdapter.add(message = messageTyping)
            scrollToBottom()
        } else {
            mAdapter.removeItem()
        }
    }

    private fun scrollToBottom() {
        if (mPresenter.listMessage.isNotEmpty()) {
            rvChat.scrollToPosition(mPresenter.listMessage.size - 1)
        }
    }
}