package com.byphs.firebasept.itemDeco

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class CirclePagerIndicatorDecoration : ItemDecoration() {
    private val colorActive = -0x22000000
    private val colorInactive = 0x33000000

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorHeight = (DP * 16).toInt()

    /**
     * Indicator stroke width.
     */
    private val mIndicatorStrokeWidth =
        DP * 2

    /**
     * Indicator width.
     */
    private val mIndicatorItemLength = DP * 2

    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding =
        DP * 8

    /**
     * Some more natural animation interpolation
     */
    //    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private val mPaint = Paint()
    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDrawOver(c, parent, state)
        val itemCount =
            if (parent.adapter!!.itemCount == 1) 1 else (parent.adapter!!
                .getItemCount()) / 3

        // center horizontally, calculate width and subtract half from center
        val totalLength = mIndicatorItemLength * itemCount
        val paddingBetweenItems =
            Math.max(0, itemCount - 1) * mIndicatorItemPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

        // center vertically in the allotted space
        val indicatorPosY = parent.height - mIndicatorHeight / 2f
        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)

        // find active page (which should be highlighted)
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        val activePosition = layoutManager!!.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }
        Log.d("ActivePosition",activePosition.toString())
        //        activePosition %= itemCount;

        // find offset of active page (if the user is scrolling)
        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild!!.left
        val width = activeChild.width
        val right = activeChild.right

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
//        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        val i:Int = if(activePosition < 3){
            0
        }
        else if(activePosition < 6) 1
        else 2
//        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition % itemCount, progress);
        drawHighlights(c, indicatorStartX, indicatorPosY, i)
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        mPaint.color = colorInactive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        var start = indicatorStartX
        for (i in 0 until itemCount) {
            c.drawCircle(start, indicatorPosY, mIndicatorItemLength / 2f, mPaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int
    ) {
        mPaint.color = colorActive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

//        if (progress == 0F) {
        // no swipe, draw a normal indicator
        val highlightStart = indicatorStartX + itemWidth * highlightPosition
        c.drawCircle(highlightStart, indicatorPosY, mIndicatorItemLength / 2f, mPaint)

//        } else {
//            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
//            // calculate partial highlight
//            float partialLength = mIndicatorItemLength * progress + mIndicatorItemPadding*progress;
//
//            c.drawCircle(highlightStart + partialLength, indicatorPosY, mIndicatorItemLength / 2F, mPaint);
//        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mIndicatorHeight
    }

    companion object {
        private val DP =
            Resources.getSystem().displayMetrics.density
    }

    init {
        mPaint.strokeWidth = mIndicatorStrokeWidth
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
    }
}