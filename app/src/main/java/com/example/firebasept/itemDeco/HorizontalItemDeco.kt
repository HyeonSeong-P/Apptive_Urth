package com.example.firebasept.itemDeco

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HorizontalItemDeco:RecyclerView.ItemDecoration {
    var divWidth = 0
    var divHeight = 0
    constructor(context: Context){
        divWidth = dpToPx(context,12)
        divHeight = dpToPx(context,14)
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.getResources().getDisplayMetrics()
        ).toInt()
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // Column Index
        var index = GridLayoutManager.LayoutParams(view.layoutParams).spanIndex

        //Item 포지션
        var position = parent.getChildLayoutPosition(view)
        if(position != 0){
            outRect.left = divWidth
        }
        //outRect.left = divWidth
        //outRect.right = divWidth
        outRect.top = divHeight
    }




}