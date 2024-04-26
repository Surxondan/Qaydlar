package uz.surxondan.qaydlar

import java.io.Serializable

data class ToDoMAdel(
    val title:String,
    var muhim:Boolean=false,
    var time:Long =System.currentTimeMillis(),
    var isfinish :Boolean =false

):Serializable
