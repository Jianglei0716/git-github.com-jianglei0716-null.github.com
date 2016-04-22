package com.dylan.ablum

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.actionbar_footer.*
import kotlinx.android.synthetic.main.choose_ablum_layout.*
import kotlinx.android.synthetic.main.multi_selector_activity.*
import java.io.File
import java.util.*


/**
 * Created by jianglei on 16/4/21.
 */
class MultiSelectorActivity : AppCompatActivity(), OnLoadFinish {
    var mResultFolder: ArrayList<Folder>? = null
    var isNeedCrop: Boolean? = false
    var isMuiltSelect: Boolean? = false
    override fun onLoadSuccess(mResultFolder: ArrayList<Folder>) {
        this.mResultFolder = mResultFolder
        initData(mResultFolder)
    }

    override fun onLoadFailed() {

    }

    var width: Int? = 0
    var height: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multi_selector_activity)
        image_grid.layoutManager = GridLayoutManager(this, 3)
        image_grid.addItemDecoration(SimpleDividerItemDecoration(this))
//        image_grid.viewTreeObserver.addOnGlobalLayoutListener {
//            width = image_grid.width
//            height = image_grid.height
//        }
        isMuiltSelect = intent.getBooleanExtra(ImageConstants.NEED_MULTI, false)
        isNeedCrop = intent.getBooleanExtra(ImageConstants.NEED_CROP, false)
        more_ablum.setOnClickListener(View.OnClickListener { showMoreAlbumPop() })
        supportLoaderManager.initLoader(ImageConstants.LOADER_ALL, null, ImageLoaderCallback(this, this))
    }

    /**show choose album popupwindow*/
    var popupWindow: PopupWindow? = null

    fun showMoreAlbumPop() {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        } else {
            var view: View = LayoutInflater.from(this).inflate(R.layout.choose_ablum_layout, null, true)
            popupWindow = PopupWindow(view)
            var folderList = view.findViewById(R.id.folderList) as RecyclerView
            var footView = findViewById(R.id.footView) as View
            folderList.layoutManager = LinearLayoutManager(this)
            folderList.adapter = FolderAdapter(this, mResultFolder)
            val location = IntArray(2)
            footView.getLocationOnScreen(location)
            popupWindow!!.showAtLocation(footView, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow!!.height)
        }
    }

    /**adapter for folder*/
    class FolderAdapter() : RecyclerView.Adapter<FolderAdapter.FolderHolder>() {
        var context: Context? = null
        var data: ArrayList<Folder>? = null

        constructor(context: Context, data: ArrayList<Folder>?) : this() {
            this.context = context
            this.data = data
        }

        override fun onBindViewHolder(holder: FolderHolder?, position: Int) {
            Glide.with(context).load(File(data!![position].path)).override(100, 100).into(holder!!.folderCoverImage)
            holder!!.folderCount!!.text = "${data!![position].images!!.size}张"
            holder!!.folderNameText!!.text = data!![position].name
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FolderHolder? {
            return FolderHolder(LayoutInflater.from(context).inflate(R.layout.item_folder_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return data!!.size
        }

        class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var folderCoverImage: ImageView? = null
            var folderNameText: TextView? = null
            var folderCount: TextView? = null

            init {
                folderCoverImage = itemView.findViewById(R.id.folderCover) as ImageView
                folderNameText = itemView.findViewById(R.id.folderName) as TextView
                folderCount = itemView.findViewById(R.id.folderCount) as TextView
            }
        }

    }

    /** call when OnLoadFinish.onLoadSuccess*/
    fun initData(folders: ArrayList<Folder>) {
        if (folders == null || folders.size == 0) return
        if (folders[0].images == null || folders[0].images!!.size == 0) return
        var adapter = ImageAdapter(this, folders[0].images!!)
        image_grid.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    /**adapter for image*/
    class ImageAdapter() : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
        var context: Context? = null
        var images: ArrayList<Image>? = null

        constructor(context: Context, images: ArrayList<Image>) : this() {
            this.context = context
            this.images = images
        }

        override fun onBindViewHolder(holder: ImageHolder?, position: Int) {
            Glide.with(context).load(images!![position].path).override(240, 240).into(holder!!.imageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageHolder? {
            return ImageHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return images!!.size
        }

        class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var imageView: ImageView? = null
            var checkBox: Button? = null

            init {
                imageView = itemView.findViewById(R.id.image) as ImageView
                checkBox = itemView.findViewById(R.id.checkbox) as Button
            }
        }

    }
}
