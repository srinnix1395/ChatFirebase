package com.example.ominext.chatfirebase.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Ominext on 8/2/2017.
 */
class User() : Parcelable {
    var uid: String? = null

    var email: String? = null

    var name: String? = null

    var photo: String? = null

    var status: String = Status.ONLINE.name

    var lastOnline: Long = 0

    constructor(uid: String?, email: String?, name: String?, photo: String?, status: String, lastOnline: Long = 0) : this() {
        this.uid = uid
        this.email = email
        this.name = name
        this.photo = photo
        this.status = status
        this.lastOnline = lastOnline
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uid)
        dest.writeString(email)
        dest.writeString(name)
        dest.writeString(photo)
        dest.writeString(status)
        dest.writeLong(lastOnline)
    }
}

enum class Status {
    OFFLINE,
    ONLINE
}