package uz.surxondan.qaydlar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.filament.Colors
import com.google.android.filament.View
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class RvHelper(
    private  val toDoAdapter: ToDoAdapter,
    private val context: Context
):ItemTouchHelper.Callback(

) {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val accessOnmove =ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val accessOnswipe =ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        return makeMovementFlags(accessOnmove,accessOnswipe)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition= viewHolder.adapterPosition
        val toPosition=target.adapterPosition
            toDoAdapter.notifyItemMoved(fromPosition,toPosition)
        toDoAdapter.swapOnMoved(fromPosition,toPosition,context)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        toDoAdapter.deleteItemFromList(viewHolder.adapterPosition,context)
        toDoAdapter.notifyItemRemoved(viewHolder.adapterPosition)


    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c, recyclerView,
            viewHolder, dX, dY, actionState, isCurrentlyActive
        )
            .addSwipeRightActionIcon(R.drawable.delete_24)
            .addSwipeLeftActionIcon(R.drawable.delete_24)
            .addSwipeLeftBackgroundColor(Color.RED)
            .addSwipeRightBackgroundColor(Color.RED)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}

