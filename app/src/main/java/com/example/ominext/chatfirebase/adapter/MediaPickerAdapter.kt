package com.example.ominext.chatfirebase.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.viewholder.MediaLocalViewHolder
import com.example.ominext.chatfirebase.model.MediaLocal
import java.util.*

/**
 * Created by anhtu on 4/24/2017.
 */

class MediaPickerAdapter(private val arrayList: ArrayList<MediaLocal?>,
                         private val listener: (position: Int) -> Unit) : RecyclerView.Adapter<MediaLocalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaLocalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_picker, parent, false)
        return MediaLocalViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MediaLocalViewHolder, position: Int, payloads: List<Any>?) {
        super.onBindViewHolder(holder, position, payloads)
        val size = payloads!!.size

        if (size == 0) {
            onBindViewHolder(holder, position)
            return
        }

        if (payloads[size - 1] is Boolean) {
            holder.bindSelected(payloads[size - 1] as Boolean)
        }
    }

    override fun onBindViewHolder(holder: MediaLocalViewHolder, position: Int) {
        holder.bindData(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
