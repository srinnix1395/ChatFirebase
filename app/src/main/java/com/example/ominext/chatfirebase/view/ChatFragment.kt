package com.example.ominext.plaidfork.ui.chat.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.ChatAdapter
import com.example.ominext.chatfirebase.presenter.ChatPresenter
import com.example.ominext.chatfirebase.widget.EndlessScrollUpListener

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

    val mPresenter: ChatPresenter = ChatPresenter()

    lateinit var mAdapter: ChatAdapter

    init {
        mPresenter.addView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)
        mPresenter.getData(arguments)

        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        val scrollListener: EndlessScrollUpListener = object : EndlessScrollUpListener(layoutManager) {
            override fun onLoadMore() {
                mPresenter.onLoadMessage()
            }
        }
        rvChat.layoutManager = layoutManager
        rvChat.addOnScrollListener(scrollListener)
        mAdapter = ChatAdapter(context, mPresenter.listMessage, mPresenter.urlImage, {

        })

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
    }

    @OnClick(R.id.imageview_send)
    fun onClickSend() {
        mPresenter.sendMessage(etMessage.text.toString(), imvSend.drawable.level)
    }
}