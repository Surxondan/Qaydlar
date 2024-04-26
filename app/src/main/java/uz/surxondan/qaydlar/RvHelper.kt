package uz.surxondan.qaydlar

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

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
}