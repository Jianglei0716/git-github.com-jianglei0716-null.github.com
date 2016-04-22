package com.dylan.ablum

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import java.io.File
import java.util.*

/**
 * Created by jianglei on 16/4/22.
 */
class ImageLoaderCallback(context: Context, onLoadFinish: OnLoadFinish) : LoaderManager.LoaderCallbacks<Cursor> {

    val LOADER_ALL = 0
    val LOADER_CATEGORY = 1
    val IMAGE_PROJECTION = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media._ID)
    val context: Context = context
    val onLoadFinish: OnLoadFinish = onLoadFinish
    var folders: ArrayList<Folder>? = null

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        if (id == LOADER_ALL) {
            val cursorLoader = CursorLoader(context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, "${IMAGE_PROJECTION[2]} DESC")
            return cursorLoader
        } else if (id == LOADER_CATEGORY) {
            val cursorLoader = CursorLoader(context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    "${IMAGE_PROJECTION[0]} like '%${ args!!.getString("path")} %'", null, "${IMAGE_PROJECTION[2]} DESC")

            return cursorLoader
        }
        return null
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        resolveCursor(data)
    }

    fun resolveCursor(cursor: Cursor?) {
        if (onLoadFinish == null) {
            return ;
        }
        if (cursor == null) {
            onLoadFinish.onLoadFailed()
        }
        folders = ArrayList<Folder>()
        val topFolder = Folder()
        topFolder.path = ""
        topFolder.name = context.resources.getString(R.string.all_pic)
        folders!!.add(topFolder)
        if (cursor!!.count == 0) {
            onLoadFinish.onLoadSuccess(folders!!)
            return
        }
        cursor.moveToFirst()
        var a:Long=0
        do {
            val path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
            val dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
            val file = File(path)
            if (file.length() != a) {
                val image = Image(path, name, dateTime)
                topFolder.addImage(image)
                val folderFile = file.parentFile
                var folder = getFolderByPath(folderFile.path)
                if (folder == null) {
                    folder = Folder()
                    folder!!.name = folderFile.name
                    folder!!.path = folderFile.absolutePath
                    folders!!.add(folder)
                }
                folder!!.addImage(image)
            }
        } while (cursor.moveToNext())
        onLoadFinish.onLoadSuccess(folders!!)
    }

    fun getFolderByPath(path: String): Folder? {
        var i = 1
        val count = folders!!.size
        while (i < count) {
            val folder = folders!!.get(i)
            if (folder.path.equals(path))
                return folder
            i++
        }
        return null
    }

}