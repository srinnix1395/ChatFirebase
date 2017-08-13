package com.example.ominext.chatfirebase.helper

import android.content.Context
import android.provider.MediaStore
import com.example.ominext.chatfirebase.model.MediaLocal
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by anhtu on 4/25/2017.
 */

class MediaPickerHelper {

    fun getLocalImage(mContext: Context): Single<ArrayList<MediaLocal>> {
        return Single.fromCallable<ArrayList<MediaLocal>> {
            val cursor = mContext.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionImage, null, null, MediaStore.Images.Media.DATE_ADDED)

            if (cursor == null) {
                return@fromCallable null
            }

            val temp = ArrayList<MediaLocal>(cursor.count)
            var file: File

            if (cursor.moveToLast()) {
                do {
                    val id = cursor.getLong(0)
                    val name = cursor.getString(1)
                    val path = cursor.getString(2)
                    val isGIF = cursor.getString(3).equals("image/gif", ignoreCase = true)

                    file = File(path)
                    if (file.exists()) {
                        val image = MediaLocal(id, name, path, isGIF)
                        temp.add(image)
                    }

                } while (cursor.moveToPrevious())
            }
            cursor.close()

            temp
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getImageCapture(mContext: Context, path: String): Single<MediaLocal?> {
        return Single.fromCallable {
            var image: MediaLocal? = null

            val cursor = mContext.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.MIME_TYPE),
                    "_data = '$path'", null, MediaStore.Images.Media.DATE_ADDED)

            if (cursor == null) {
                return@fromCallable null
            }

            val file: File

            if (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                val name = cursor.getString(1)
                val isGIF = cursor.getString(2).equals("image/gif", ignoreCase = true)

                file = File(path)
                if (file.exists()) {
                    image = MediaLocal(id, name, path, isGIF)
                }
            }
            cursor.close()

            image
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLocalVideo(mContext: Context): Single<ArrayList<MediaLocal>> {
        return Single.fromCallable<ArrayList<MediaLocal>> {
            val cursor = mContext.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideo, null, null, MediaStore.Video.Media.DATE_ADDED)

            if (cursor == null) {
                return@fromCallable arrayListOf()
            }

            val temp = ArrayList<MediaLocal>(cursor.count)
            var file: File

            if (cursor.moveToLast()) {
                do {
                    val id = cursor.getLong(0)
                    val name = cursor.getString(1)
                    val path = cursor.getString(2)
                    val duration = cursor.getInt(3)
                    val urlThumbnail = cursor.getString(4)
                    val size = cursor.getInt(5)

                    file = File(path)
                    if (file.exists()) {
                        val image = MediaLocal(id, name, path, true, duration, urlThumbnail, size)
                        temp.add(image)
                    }

                } while (cursor.moveToPrevious())
            }
            cursor.close()

            return@fromCallable temp
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getVideoCapture(mContext: Context, path: String): Single<MediaLocal?> {
        return Single.fromCallable {
            var video: MediaLocal? = null

            val cursor = mContext.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION, MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Media.SIZE),
                    "_data = '$path'", null, MediaStore.Video.Media.DATE_ADDED)

            if (cursor == null) {
                return@fromCallable null
            }

            val file: File

            if (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                val name = cursor.getString(1)
                val duration = cursor.getInt(2)
                val urlThumbnail = cursor.getString(3)
                val size = cursor.getInt(4)

                file = File(path)
                if (file.exists()) {
                    video = MediaLocal(id, name, path, true, duration, urlThumbnail, size)
                }
            }
            cursor.close()

            video
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {

        @JvmField val projectionImage = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE)

        @JvmField val projectionVideo = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION, MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Media.SIZE)
    }
}
