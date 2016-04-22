package com.dylan.ablum

import java.util.*

/**
 * Created by jianglei on 16/4/22.
 */
class Folder() {
    var path: String? = ""
    var name: String ? = ""
    var images: ArrayList<Image>? = null


    fun addImage(image: Image?) {
        if (image == null)
            return
        if (images == null)
            images = ArrayList<Image>()
        images!!.add(image)
    }

    fun getCover(): Image? {
        if (images == null || images!!.size == 0)
            return null
        return images!!.get(0)
    }
}
