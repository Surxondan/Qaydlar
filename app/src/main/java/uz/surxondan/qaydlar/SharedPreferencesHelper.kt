package uz.surxondan.qaydlar

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferenceHelper {
    private val gson by lazy { Gson() }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("todoshared", Context.MODE_PRIVATE)
    }

    fun saveToDo(todoList: List<ToDoMAdel>, context: Context) {
        getSharedPreferences(context).edit()?.putString(TODO_KEY, gson.toJson(todoList))?.apply()
    }


    fun getToDoList(context: Context): List<ToDoMAdel> {
        val json = getSharedPreferences(context).getString(TODO_KEY, "")
        return if (!json.isNullOrEmpty()) {
            gson.fromJson(json, object : TypeToken<List<ToDoMAdel>>() {}.type)
        } else {
            emptyList()
        }
    }

        fun  clearCache(context: Context){
            getSharedPreferences(context).edit().clear().apply()
        }
    private const val TODO_KEY = "to_do_key"
}

