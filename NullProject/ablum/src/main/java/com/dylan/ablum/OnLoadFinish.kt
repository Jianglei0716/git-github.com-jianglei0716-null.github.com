package com.dylan.ablum

import java.util.*

/**
 * Created by jianglei on 16/4/22.
 */
interface OnLoadFinish{
    /**
     * 加载成功

     * @param mResultFolder 图片所在的文件夹
     */
    abstract fun onLoadSuccess(mResultFolder: ArrayList<Folder>)


    /**
     * 加载失败
     */
    abstract fun onLoadFailed()
}