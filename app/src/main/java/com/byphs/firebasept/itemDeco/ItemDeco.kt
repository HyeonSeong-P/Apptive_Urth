package com.byphs.firebasept.itemDeco

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ItemDeco:RecyclerView.ItemDecoration {
    var divWidth = 0
    var divHeight = 0
    constructor(context: Context){
        divWidth = dpToPx(context,8)
        divHeight = dpToPx(context,26)
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
        //Log.d("인덱스 뭔데 ㅅㅂ",index.toString())

        //Item 포지션
        var position = parent.getChildLayoutPosition(view)

        //Log.d("포지션 뭔데 ㅅㅂ",position.toString())


        if(position%2 == 0){
            outRect.right = divWidth
        }
        else{
            outRect.left = divWidth
        }

        if(position < 2){
            outRect.top = 0;
        }
        else{

            outRect.top = divHeight
        }
    }




}