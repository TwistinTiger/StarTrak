package com.twistentiger.startrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText

class UpdateActivity : AppCompatActivity()
{
    private lateinit var titleUpdate: TextInputEditText
    private lateinit var authorUpdate: TextInputEditText
    private lateinit var isbnUpdate: TextInputEditText
    private lateinit var genreUpdate: TextInputEditText
    private lateinit var notesUpdate: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        title = "Update Book"

        val book: Book = intent.getSerializableExtra("book") as Book

        titleUpdate = findViewById(R.id.title_editText)
        authorUpdate = findViewById(R.id.author_editText)
        isbnUpdate = findViewById(R.id.isbn_editText)
        genreUpdate = findViewById(R.id.genre_editText)
        notesUpdate = findViewById(R.id.notes_editText)

        titleUpdate.setText(book.title)
        authorUpdate.setText(book.author)
        isbnUpdate.setText(book.isbn.toString())
        genreUpdate.setText(book.genre)
        notesUpdate.setText(book.notes)

    }
}


