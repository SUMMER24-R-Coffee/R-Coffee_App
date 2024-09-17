package datlowashere.project.rcoffee.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.customview.widget.ViewDragHelper

class SwipeRevealLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val dragHelper: ViewDragHelper
    private lateinit var contentView: View
    private lateinit var actionView: View
    private var dragOffset: Int = 0

    init {
        dragHelper = ViewDragHelper.create(this, 1.0f, DragHelperCallback())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = getChildAt(0)
        actionView = getChildAt(1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(contentView, widthMeasureSpec, heightMeasureSpec)
        measureChild(actionView, widthMeasureSpec, heightMeasureSpec)
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(maxWidth, maxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val contentWidth = contentView.measuredWidth
        val contentHeight = contentView.measuredHeight
        val actionWidth = actionView.measuredWidth
        contentView.layout(0 - dragOffset, 0, contentWidth - dragOffset, contentHeight)
        actionView.layout(
            contentWidth - dragOffset,
            0,
            contentWidth + actionWidth - dragOffset,
            contentHeight
        )
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        dragHelper.processTouchEvent(ev)
        return true
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == contentView
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            dragOffset = left
            actionView.translationX = left.toFloat()
            invalidate()
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val finalLeft = if (-dragOffset > actionView.width / 2) -actionView.width else 0
            dragHelper.settleCapturedViewAt(finalLeft, releasedChild.top)
            invalidate()
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left.coerceIn(-actionView.width, 0)
        }
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }
}
