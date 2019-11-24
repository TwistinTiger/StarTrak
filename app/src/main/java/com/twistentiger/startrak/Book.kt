package com.twistentiger.startrak

data class Book(val title: String, val author: String,
                val isbn: Long, val genre: String)
{
    //Kotlin no argument constructor
    constructor(): this("","",0,"")
}