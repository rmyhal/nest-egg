package com.rmyhal.nestegg.ui.global

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rmyhal.nestegg.R

class SwipeToDeleteCallback(context: Context, private val onSwiped: (Int) -> Unit) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    private val backgroundDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.red))
    private val iconDelete = ContextCompat.getDrawable(context, R.drawable.ic_delete_outline_24)!!

    private val iconMargin = 16.dp(context).toInt()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView

        val iconTop = itemView.top + (itemView.height - iconDelete.intrinsicHeight) / 2
        val iconBottom = iconTop + iconDelete.intrinsicHeight

        when {
            // swiping to the right
            dX > 0 -> {
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + iconDelete.intrinsicWidth
                iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                backgroundDrawable.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )
            }
            // swiping to the left
            dX < 0 -> {
                val iconLeft = itemView.right - iconMargin - iconDelete.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                backgroundDrawable.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            else -> {
                iconDelete.setBounds(0, 0, 0, 0)
                backgroundDrawable.setBounds(0, 0, 0, 0)
            }
        }
        backgroundDrawable.draw(c)
        iconDelete.draw(c)
    }

    private fun Int.dp(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}