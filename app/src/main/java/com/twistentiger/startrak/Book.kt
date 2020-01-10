package com.twistentiger.startrak

import java.io.Serializable

data class Book(val title: String, val author: String,
                val isbn: Long, val genre: String, val notes: String) : Serializable
{
    //Kotlin no argument constructor
    constructor(): this("","",0,"","")
}