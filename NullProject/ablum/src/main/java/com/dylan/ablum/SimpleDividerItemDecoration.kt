package com.dylan.ablum

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jianglei on 16/4/22.
 */
class SimpleDividerItemDecoration() : RecyclerView.ItemDecoration() {
    var mDivider: Drawable? = null

    constructor(context: Context) : this() {
        mDivider = context.resources.getDrawable(R.drawable.line_divider)
    }

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        val left = parent!!.paddingLeft
        val right = parent!!.width - parent!!.paddingRight

        val childCount = parent!!.childCount
        for (i in 0..childCount - 1) {
            val child = parent!!.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight

            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect!!.top = 8
        outRect!!.left = 4
        outRect!!.right = 4
    }
}
