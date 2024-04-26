package uz.surxondan.qaydlar
import android.os.Build
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import uz.surxondan.qaydlar.databinding.ActivityAddToDoBinding

class AddToDoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddToDoBinding
    private val toDoList = mutableListOf<ToDoMAdel>()
    private var toDoType:ToDo=ToDo.ADD
    private var todoModelEdit:ToDoMAdel?=null
    private var todoMadelposition:Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntenetData()
        onCliks()
        initData()
    }


    private fun getIntenetData() {
        val todoTypenama= intent?.getStringExtra(MainActivity.TODO_type)?:ToDo.ADD.name
        toDoType=ToDo.valueOf(todoTypenama)
        todoModelEdit= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getSerializableExtra(MainActivity.TODO_model_edit,ToDoMAdel::class.java)
            else intent?.getSerializableExtra(MainActivity.TODO_model_edit) as? ToDoMAdel

        todoMadelposition=intent?.getIntExtra(MainActivity.TODO_model_edit_postion,-1) ?: -1
        when(toDoType){
            ToDo.ADD -> {
                binding.btnAdd.setImageResource(R.drawable.baseline_check_24)
            }
            ToDo.EDIT -> {

                binding.etTodo.setText(todoModelEdit?.title?:"Default")
                binding.chekkedMuhim.isChecked=todoModelEdit?.isfinish?:false
                binding.btnAdd.setImageResource(R.drawable.baseline_edit_24)
                binding.toolbar.title="Qaydni tahrirlayapsiz"
            }
        }

    }

    private fun onCliks() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            validate()
            when(toDoType){
                ToDo.ADD -> saveToDo()
                ToDo.EDIT -> {
                    val todomadel=ToDoMAdel(binding.etTodo.text.toString(),binding.chekkedMuhim.isChecked)
                        if(todoMadelposition!=-1){
                            toDoList[todoMadelposition]=todomadel
                            SharedPreferenceHelper.saveToDo(toDoList,this@AddToDoActivity)
                            finish()
                        }
                }
            }
        }
    }

    private fun saveToDo() {
        val title = binding.etTodo.text.toString()
        if (title.isNotEmpty()) {
            val toDoModel = ToDoMAdel(title, binding.chekkedMuhim.isChecked)
            toDoList.add(toDoModel)
            SharedPreferenceHelper.saveToDo(toDoList, this)
            finish()
        }
    }

    private fun validate() {
        if (binding.etTodo.text.isNullOrEmpty() || binding.etTodo.text.isNullOrBlank()) {
            Toast.makeText(this, "Ma`lumot kiriting", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        toDoList.clear()
        toDoList.addAll(SharedPreferenceHelper.getToDoList(this))
    }
}
