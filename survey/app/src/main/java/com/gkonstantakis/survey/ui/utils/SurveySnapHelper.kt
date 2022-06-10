package com.gkonstantakis.survey.ui.utils

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class SurveySnapHelper: LinearSnapHelper() {
    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        return super.onFling(velocityX, velocityY)
    }

    override fun calculateScrollDistance(velocityX: Int, velocityY: Int): IntArray {
        return super.calculateScrollDistance(velocityX, velocityY)
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        return super.calculateDistanceToFinalSnap(layoutManager, targetView)
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return super.findSnapView(layoutManager)
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        return super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
    }
}