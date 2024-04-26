package uz.surxondan.qaydlar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.surxondan.qaydlar.databinding.ItemToDoBinding
import java.util.Collections

class ToDoAdapter(
    private val onClick: (position: Int,isfinishchekked:Boolean) -> Unit,


) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    val dateList = mutableListOf<ToDoMAdel>()
             var onLongClick:((todoModel:ToDoMAdel,position:Int)->Unit)?=null
    fun notifyItemDataChanged(position:Int,context: Context,isfinishchekked: Boolean){

        dateList[position].isfinish=isfinishchekked
        SharedPreferenceHelper.saveToDo(dateList,context)
        notifyItemChanged(position)
    }

    inner class ToDoViewHolder(
        private val binding: ItemToDoBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SuspiciousIndentation")
        fun bind(toDoModel: ToDoMAdel) {
            binding.tvTitle.text = toDoModel.title
            binding.chekkedTayyor.isChecked=toDoModel.isfinish
            binding.viewFinish.visibility = if (toDoModel.isfinish) View.VISIBLE else View.INVISIBLE
            binding.ivMuhim.visibility = if (toDoModel.muhim) View.VISIBLE else View.INVISIBLE
                binding.chekkedTayyor.setOnClickListener{
                    onClick(adapterPosition,binding.chekkedTayyor.isChecked)
                }
           binding.tvTitle.setOnClickListener{
               onLongClick?.invoke(toDoModel,adapterPosition)

           }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(dateList[position])
    }


    fun loadDate(newList: List<ToDoMAdel>) {
        dateList.clear()
        dateList.addAll(newList)
        notifyDataSetChanged()

    }
    fun swapOnMoved(frompos:Int,topos:Int,context: Context){
               Collections.swap(dateList,frompos,topos)

        SharedPreferenceHelper.saveToDo(dateList,context)
    }
    fun deleteItemFromList( position: Int,context: Context){
        dateList.removeAt(position)
        SharedPreferenceHelper.saveToDo(dateList,context)

    }

    //search uchun
    @SuppressLint("NotifyDataSetChanged")
    fun  search(text:String?, context: Context){
        text?.let {
        SharedPreferenceHelper.getToDoList(context).toMutableList().filter { it.title.lowercase().startsWith(text.lowercase()) }.let {
            loadDate(it)
        }
           // notifyDataSetChanged()
        }?:loadDate(SharedPreferenceHelper.getToDoList(context))
    }
}

