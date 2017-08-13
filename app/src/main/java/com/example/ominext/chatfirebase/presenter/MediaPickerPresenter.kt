package com.example.ominext.chatfirebase.presenter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.util.LongSparseArray
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.model.MessageImageLocal
import com.example.ominext.chatfirebase.view.MediaPickerFragment
import com.example.ominext.chatfirebase.widget.addToCompositeDisposable
import com.example.ominext.chatfirebase.widget.toast
import com.example.ominext.chatfirebase.helper.MediaPickerHelper
import com.example.ominext.chatfirebase.model.MediaLocal
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*

/**
 * Created by anhtu on 4/25/2017.
 */

class MediaPickerPresenter(val view: MediaPickerFragment) {

    companion object {
        @JvmField val PERMISSIONS_REQUEST_READ_EXTERNAL = 14
        @JvmField val PERMISSIONS_REQUEST_CAMERA = 15
        @JvmField val REQUEST_CODE_TAKE_PICTURE = 16
        @JvmField val REQUEST_CODE_TAKE_VIDEO = 17
    }

    var numberImage = 0
    var numberVideo = 0
    val listImageSelected = LongSparseArray<MediaLocal>()

    private lateinit var mHelper: MediaPickerHelper
    private var fileCapture: File? = null
    var mediaType: Int = 0
    private var limit: Int = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getData(bundle: Bundle) {
        mediaType = bundle.getInt(ChatConstant.KEY_MEDIA_TYPE, ChatConstant.TYPE_IMAGE)
        limit = bundle.getInt(ChatConstant.KEY_LIMIT, 1)

        val arrayList: ArrayList<MediaLocal>? = bundle.getParcelableArrayList(ChatConstant.KEY_MEDIA)
        arrayList?.let {
            for (mediaLocal in arrayList) {
                listImageSelected.put(mediaLocal.id, mediaLocal)
                if (mediaLocal.isVideo) {
                    numberVideo++
                } else {
                    numberImage++
                }
            }
        }
    }

    fun checkPermissionStorage(activity: FragmentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isHasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

            if (!isHasPermission) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_READ_EXTERNAL)
            } else {
                Handler().postDelayed({ this.getMedia(activity) }, 300)
            }
        } else {
            Handler().postDelayed({ this.getMedia(activity) }, 300)
        }
    }

    fun getMedia(context: Context) {
        if (mediaType == ChatConstant.TYPE_IMAGE) {
            getImage(context)
        } else {
            getVideo(context)
        }
    }

    fun getVideo(context: Context) {
        mHelper.getLocalVideo(context)
                .subscribe({ imageLocals ->
                    if (imageLocals == null) {
                        view.onLoadFail(R.string.error_common)
                    } else {
                        var i = 0
                        for (mediaLocal in imageLocals) {
                            if (i == listImageSelected.size()) {
                                break
                            }
                            if (listImageSelected.indexOfKey(mediaLocal.id) >= 0) {
                                i++
                                mediaLocal.isSelected = true
                            }
                        }
                        view.onLoadSuccess(imageLocals)
                    }
                }, { throwable ->
                    throwable.printStackTrace()
                    view.onLoadFail(R.string.error_common)
                })
                .addToCompositeDisposable(compositeDisposable)
    }

    fun getImage(context: Context) {
        mHelper.getLocalImage(context)
                .subscribe({ imageLocals ->
                    if (imageLocals == null) {
                        view.onLoadFail(R.string.error_common)
                    } else {
                        var i = 0
                        for (mediaLocal in imageLocals) {
                            if (i == listImageSelected.size()) {
                                break
                            }
                            if (listImageSelected.indexOfKey(mediaLocal.id) >= 0) {
                                i++
                                mediaLocal.isSelected = true
                            }
                        }
                        view.onLoadSuccess(imageLocals)
                    }
                }, { throwable ->
                    throwable.printStackTrace()
                    view.onLoadFail(R.string.error_common)
                })
                .addToCompositeDisposable(compositeDisposable)
    }

    fun onClickCamera(mediaPickerFragment: MediaPickerFragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isHasPermission = ContextCompat.checkSelfPermission(mediaPickerFragment.activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

            if (!isHasPermission) {
                ActivityCompat.requestPermissions(mediaPickerFragment.activity,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSIONS_REQUEST_CAMERA)
            } else {
                openCamera(mediaPickerFragment)
            }
        } else {
            openCamera(mediaPickerFragment)
        }

    }

    fun openCamera(mediaPickerFragment: MediaPickerFragment) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(mediaPickerFragment.context.packageManager) != null) {

            if (mediaType == ChatConstant.TYPE_IMAGE) {
                val publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

                val file = "IMAGE_CAPTURE_" + System.currentTimeMillis() + ".jpg"
                fileCapture = File(publicDirectory, file)

                val imageCapture = Uri.fromFile(fileCapture)

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapture)

                mediaPickerFragment.startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE)
            } else {
                val publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)

                val file = "VIDEO_CAPTURE_" + System.currentTimeMillis() + ".mp4"
                fileCapture = File(publicDirectory, file)

                val videoCapture = Uri.fromFile(fileCapture)

                val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoCapture)

                mediaPickerFragment.startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_VIDEO)
            }
        } else {
            (R.string.error_no_camera).toast(mediaPickerFragment.context)
        }
    }

    fun displayMedia(context: Context, mListMedia: ArrayList<MediaLocal?>) {
        val mediaUri = Uri.fromFile(fileCapture)
        if (mediaUri != null) {
            MediaScannerConnection.scanFile(context, arrayOf(mediaUri.path), null) { path, uri ->
                val single: Single<MediaLocal?>
                if (mediaType == ChatConstant.TYPE_IMAGE) {
                    single = mHelper.getImageCapture(context, fileCapture!!.path)
                } else {
                    single = mHelper.getVideoCapture(context, fileCapture!!.path)
                }

                single.subscribe({ mediaLocal, throwable ->
                    mListMedia.add(0, mediaLocal)
                    view.insertMediaLocal()
                    throwable.printStackTrace()
                })
            }
        }
    }

    fun onClickMedia(context: Context, mediaLocal: MediaLocal?, position: Int) {
        mediaLocal?.let {
            if (mediaLocal.isSelected) {
                if (mediaLocal.isVideo) {
                    numberVideo--
                } else {
                    numberImage--
                }
                mediaLocal.isSelected = false
                listImageSelected.remove(mediaLocal.id)
            } else {
                if (numberImage + numberVideo == limit) {
                    String.format(Locale.getDefault(), "Số ảnh tối đa là %d ảnh", limit).toast(context)
                    return
                }

                if (mediaLocal.isVideo) {
                    numberVideo++
                } else {
                    numberImage++
                }

                mediaLocal.isSelected = true
                listImageSelected.put(mediaLocal.id, mediaLocal)
            }

            view.updateStateMedia(numberImage, numberVideo, position, mediaLocal.isSelected)
        }
    }

    fun onClickAdd(mediaPickerFragment: MediaPickerFragment) {
        if (listImageSelected.size() > 0) {
            val arrayList= (listImageSelected.size() - 1 downTo 0)
                    .map { listImageSelected.valueAt(it) }
            EventBus.getDefault().post(MessageImageLocal(arrayList))
            mediaPickerFragment.onBackPressed()
        }
    }
}
