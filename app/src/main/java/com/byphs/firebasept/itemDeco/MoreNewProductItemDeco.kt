package com.byphs.firebasept.recyclerView

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.byphs.firebasept.R


class MoreNewProductItemDeco:RecyclerView.ItemDecoration {
    var divWidth = 0
    var divWidth2 = 0
    var divHeight = 0
    constructor(context: Context){
        divWidth = dpToPx(context,12)
        divWidth2 = dpToPx(context,15)
        divHeight = dpToPx(context,12)
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

        //if(view.sourceLayoutResId == R.layout.more_new_product_brand_name_design)
        if(view.id == R.id.more_new_header){ // 이걸로 리사이클러뷰 뷰타입마다 다른 디자인인거 구분하자. xml 파일의 parent layout의 id 사용

        }
        else{
            if(index == 0){
                //outRect.right = divWidth
                outRect.left = divWidth2
            }
            else{
                outRect.left = divWidth
                //outRect.right = divWidth
            }

            if(position == 0){
                outRect.top = 0;
            }
            else{
                outRect.top = divHeight
            }
        }


    }




}