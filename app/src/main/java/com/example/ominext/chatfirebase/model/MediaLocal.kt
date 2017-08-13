package com.example.ominext.chatfirebase.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by anhtu on 4/25/2017.
 */

class MediaLocal : Parcelable {
    var id: Long = 0
    var name: String? = null
    var path: String? = null
    var isGIF: Boolean = false
    var isVideo: Boolean = false
    var duration: Int = 0
    var urlThumbnail: String? = null
    var size: Int = 0

    var isSelected = false

    constructor(id: Long, name: String, path: String, isGIF: Boolean) {
        this.id = id
        this.name = name
        this.path = path
        this.isVideo = false
        this.isGIF = isGIF
    }

    constructor(id: Long, name: String, path: String, isVideo: Boolean, duration: Int, urlThumbnail: String, size: Int) {
        this.id = id
        this.name = name
        this.path = path
        this.isVideo = isVideo
        this.duration = duration
        this.urlThumbnail = urlThumbnail
        this.size = size
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readLong()
        name = `in`.readString()
        path = `in`.readString()
        isGIF = `in`.readByte().toInt() != 0
        isVideo = `in`.readByte().toInt() != 0
        duration = `in`.readInt()
        urlThumbnail = `in`.readString()
        size = `in`.readInt()
        isSelected = `in`.readByte().toInt() != 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeString(path)
        dest.writeByte((if (isGIF) 1 else 0).toByte())
        dest.writeByte((if (isVideo) 1 else 0).toByte())
        dest.writeInt(duration)
        dest.writeString(urlThumbnail)
        dest.writeInt(size)
        dest.writeByte((if (isSelected) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<MediaLocal> = object : Parcelable.Creator<MediaLocal> {
            override fun createFromParcel(`in`: Parcel): MediaLocal {
                return MediaLocal(`in`)
            }

            override fun newArray(size: Int): Array<MediaLocal?> {
                return arrayOfNulls(size)
            }
        }
    }
}
