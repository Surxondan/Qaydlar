package uz.surxondan.qaydlar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import uz.surxondan.qaydlar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toDoAdapter: ToDoAdapter
    private  var ishide: Boolean=false
    private  var searchmenu: MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initRv()
        onCliks()

    }

    private fun EditText.hideKeyboard(){
        WindowInsetsControllerCompat(window,this).hide(WindowInsetsCompat.Type.ime())

    }
    private fun EditText.showKeyboard(){
        WindowInsetsControllerCompat(window,this).show(WindowInsetsCompat.Type.ime())
        requestFocus()
    }
    //search uchun
    private fun initSearchView(){
            binding.etSearch.visible()
        binding.etSearch.showKeyboard()
                binding.toolbar.title=""
        searchmenu?.isVisible=false
        binding.etSearch.onRightDrawableClicked {
            it.hideKeyboard()
            it.clearFocus()
            it.setText("")
            it.gone()
            binding.toolbar.title="Qaydlar"
            searchmenu?.isVisible=true
            toDoAdapter.search(null,this)
        }
        binding.etSearch.addTextChangedListener {

            val text=it?.toString()
            toDoAdapter.search(text,this)
            if (text?.isEmpty()==true|| text?.isBlank()==true){
                toDoAdapter.search(null,this)
            }

        }


    }


    // Edittextning oxirodagi icongasini bosganda yuqolishshi edittextning
    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onRightDrawableClicked(onCliked:(view:EditText)->Unit){
        this.setOnTouchListener { v, event ->
            var hasConsumed=false
            if (v is EditText){
                if (event.x >= v.width-v.totalPaddingRight){
                    if (event.action==MotionEvent.ACTION_UP){
                        onCliked(this)
                    }
                    hasConsumed=true
                }
            }
            hasConsumed
        }
    }

    private fun View.visible(){
        visibility=View.VISIBLE
    }
    private fun View.gone(){
        visibility=View.GONE
    }
    private fun View.invisible(){
        visibility=View.INVISIBLE
    }

    private fun initRv() {
        toDoAdapter= ToDoAdapter{
                position,ischeked->
                onItemClick(position,ischeked)
        }
        binding.rvToDo.adapter=toDoAdapter
        binding.rvToDo.setHasFixedSize(false)
        ItemTouchHelper(RvHelper(toDoAdapter,this)).attachToRecyclerView(binding.rvToDo)
    }

    private fun onItemClick(position: Int, ischeked: Boolean) {
               toDoAdapter.notifyItemDataChanged(position,this,ischeked)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        searchmenu=menu?.findItem(R.id.search)
        return super.onCreateOptionsMenu(menu)

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delet_all_task->{
                SharedPreferenceHelper.clearCache(this)
                onResume()
            }
            R.id.bajar_delet->{
                val dateList=SharedPreferenceHelper.getToDoList(this).toMutableList()
                dateList.removeIf{it.isfinish}
                SharedPreferenceHelper.saveToDo(dateList,this)
                onResume()
            }
            R.id.bajarilganlar->{
                val newdalist=SharedPreferenceHelper.getToDoList(this).toMutableList()
                val yangiDataList =SharedPreferenceHelper.getToDoList(this).toMutableList()
                yangiDataList.removeIf{it.isfinish}
                if(!ishide){

                    toDoAdapter.loadDate(yangiDataList)
                    ishide=true
                }
                else{
                    toDoAdapter.loadDate(newdalist)
                    ishide=false

                }

            }
            R.id.search->{
                initSearchView()
            }
            R.id.yordam->{
                val intent=Intent(this,Yordam::class.java)
                startActivity(intent)
            }
            R.id.info->{
                val intent=Intent(this,DasturHaqida::class.java)
                startActivity(intent)
            }
            R.id.manba_kodi->{
                openUrl("https://github.com/Surxondan/Qaydlar")
            }
            R.id.saralash_nomidan->{
                sortListAlphabetically()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun openUrl(link:String) {
        val uri= Uri.parse(link)
        val intent=Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        toDoAdapter.loadDate(SharedPreferenceHelper.getToDoList(this))
    }
    private fun onCliks() {
        with(binding){
            btnAdd.setOnClickListener{
                val intent= Intent(this@MainActivity, AddToDoActivity::class.java)
                intent.putExtra(TODO_type,ToDo.ADD.name)
                startActivity(intent)
            }
            toDoAdapter.onLongClick={toDoModel,position->
                   val intent=Intent(this@MainActivity,AddToDoActivity::class.java)
                intent.putExtra(TODO_type,ToDo.EDIT.name)
                intent.putExtra(TODO_model_edit,toDoModel)
                intent.putExtra(TODO_model_edit_postion,position)
                startActivity(intent)
            }
        }
    }
    private fun sortListAlphabetically() {
        val dataList = SharedPreferenceHelper.getToDoList(this).toMutableList() ?: mutableListOf()

        dataList.sortBy { it.title }
        SharedPreferenceHelper.saveToDo(dataList,this)
        toDoAdapter.loadDate(dataList)
        Toast.makeText(this, "Eslatmalar alfabet bo'yicha tartiblandi", Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val  TODO_type="to_do_type"
        const val  TODO_model_edit="to_do_model_edit"
        const val  TODO_model_edit_postion="to_do_model_edit_position"
    }
}