package com.example.ominext.chatfirebase.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import com.example.ominext.chatfirebase.R
import com.example.ominext.chatfirebase.adapter.MediaPickerAdapter
import com.example.ominext.chatfirebase.constant.ChatConstant
import com.example.ominext.chatfirebase.presenter.MediaPickerPresenter
import com.example.ominext.chatfirebase.util.Utils
import com.example.ominext.chatfirebase.widget.SpacesItemDecoration
import com.example.ominext.chatfirebase.model.MediaLocal
import java.util.*

/**
 * Created by anhtu on 4/24/2017.
 */

class MediaPickerFragment : Fragment() {

    @BindView(R.id.toolbar_image_picker)
    lateinit var toolbar: Toolbar

    @BindView(R.id.recyclerview_image)
    lateinit var rvListMedia: RecyclerView

    @BindView(R.id.progressbar_loading)
    lateinit var pbLoading: ProgressBar

    @BindView(R.id.view_stub_retry)
    lateinit var layoutRetry: ViewStub

    @BindView(R.id.textview_retry)
    lateinit var tvRetry: TextView

    private var menuItemAdd: MenuItem? = null

    private lateinit var mMediaAdapter: MediaPickerAdapter
    private lateinit var mListMedia: ArrayList<MediaLocal?>
    private val mPresenter: MediaPickerPresenter = MediaPickerPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_image_picker, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getData(arguments)
        initData()
        initChildView()
    }

    private fun initData() {
        mListMedia = ArrayList<MediaLocal?>()
        mMediaAdapter = MediaPickerAdapter(mListMedia) { position ->
            mPresenter.onClickMedia(context, mListMedia[position], position)
        }
        mPresenter.checkPermissionStorage(activity)
    }

    private fun initChildView() {
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setTitle(R.string.tap_to_select_image)

        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { view -> onBackPressed() }
        toolbar.inflateMenu(R.menu.menu_image_picker_fragment)
        menuItemAdd = toolbar.menu.findItem(R.id.menu_item_add)

        val menuItemCamera = toolbar.menu.findItem(R.id.menu_item_camera)
        if (mPresenter.mediaType == ChatConstant.TYPE_IMAGE) {
            menuItemCamera.setIcon(R.drawable.ic_photo_camera)
        } else {
            menuItemCamera.setIcon(R.drawable.ic_camera)
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item_camera -> {
                    mPresenter.onClickCamera(this@MediaPickerFragment)
                }
                R.id.menu_item_add -> {
                    mPresenter.onClickAdd(this@MediaPickerFragment)
                }
            }
            true
        }

        pbLoading.indeterminateDrawable.setColorFilter(
                ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)

        rvListMedia.adapter = mMediaAdapter

        rvListMedia.layoutManager = GridLayoutManager(context, 3)
        val decoration = SpacesItemDecoration(context, 2, 3, false)
        rvListMedia.addItemDecoration(decoration)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                MediaPickerPresenter.PERMISSIONS_REQUEST_READ_EXTERNAL -> {
                    mPresenter.getMedia(context)
                }
                MediaPickerPresenter.PERMISSIONS_REQUEST_CAMERA -> {
                    mPresenter.openCamera(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == MediaPickerPresenter.REQUEST_CODE_TAKE_PICTURE
                || requestCode == MediaPickerPresenter.REQUEST_CODE_TAKE_VIDEO)
                && resultCode == Activity.RESULT_OK) {
            mPresenter.displayMedia(context, mListMedia)
        }
    }

    fun updateStateMedia(numberImage: Int, numberVideo: Int, position: Int, selected: Boolean) {
        mMediaAdapter.notifyItemChanged(position, selected)
        if (numberVideo + numberImage == 0) {
            toolbar.setTitle(R.string.tap_to_select_image)

            if (menuItemAdd?.isVisible!!) {
                menuItemAdd?.isVisible = false
            }
        } else {
            toolbar.title = String.format(Locale.getDefault(), "%d %s",
                    mPresenter.numberImage, context.getString(R.string.image))

            if (!menuItemAdd?.isVisible!!) {
                menuItemAdd?.isVisible = true
            }
        }
    }

    fun onLoadFail(resError: Int) {
        Utils.hideProgressBar(pbLoading)

        tvRetry.setText(resError)
        layoutRetry.visibility = View.VISIBLE
    }

    fun onLoadSuccess(mediaLocals: ArrayList<MediaLocal>) {
        Utils.hideProgressBar(pbLoading)

        if (mediaLocals.isEmpty()) {
            return
        }

        rvListMedia.visibility = View.VISIBLE
        mListMedia.addAll(mediaLocals)
        mMediaAdapter.notifyItemRangeInserted(0, mediaLocals.size)

        rvListMedia.scrollToPosition(0)

        if (mPresenter.listImageSelected.size() == 0) {
            toolbar.setTitle(R.string.tap_to_select_image)
        } else {
            toolbar.title = String.format(Locale.getDefault(), "%d %s",
                    mPresenter.numberImage, getString(R.string.image))
        }
    }

    fun insertMediaLocal() {
        mMediaAdapter.notifyItemInserted(0)
        rvListMedia.scrollToPosition(0)
    }

    fun onBackPressed() {

    }
}
