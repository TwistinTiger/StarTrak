package com.twistentiger.startrak

import com.google.firebase.firestore.Exclude

data class Book(val title: String, val author: String,
                val isbn: Long, val genre: String)
{

   @Exclude private var id: String = ""

    //Kotlin no argument constructor
    constructor(): this("","",0,"")

    fun getId(): String
    {
        return id
    }

    fun setId(newId: String)
    {
        this.id = newId
    }
}