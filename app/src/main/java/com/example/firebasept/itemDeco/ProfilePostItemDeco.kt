package com.example.firebasept.itemDeco

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ProfilePostItemDeco:RecyclerView.ItemDecoration {
    var divWidth = 0
    var divHeight = 0
    constructor(context: Context){
        divWidth = dpToPx(context,1.5)
        divHeight = dpToPx(context,1.5)
    }

    private fun dpToPx(context: Context, dp: Double): Int {
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
        //outRect.right = divWidth
        // Column Index
        var index = GridLayoutManager.LayoutParams(view.layoutParams).spanIndex

        //Item 포지션
        var position = parent.getChildLayoutPosition(view)


        outRect.left = divWidth
        outRect.right = divWidth


        if(position < 3){
            outRect.top = 0;
        }
        else{
            outRect.top = divHeight
        }
    }




}