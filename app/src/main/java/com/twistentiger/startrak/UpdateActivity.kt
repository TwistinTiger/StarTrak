package com.twistentiger.startrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UpdateActivity : AppCompatActivity()
{
    private lateinit var titleUpdate: TextInputEditText
    private lateinit var authorUpdate: TextInputEditText
    private lateinit var isbnUpdate: TextInputEditText
    private lateinit var genreUpdate: TextInputEditText
    private lateinit var notesUpdate: TextInputEditText

    private lateinit var book: Book

    //another way of getting document id by passing it through intent
    private lateinit var bookId: String
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        title = "Update Book"

        book = intent.getSerializableExtra("book") as Book
        bookId = intent.getStringExtra("bookId") // getting intent that was passed from MainActivity
        database = FirebaseFirestore.getInstance()

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

    private fun updateBook()
    {
        try
        {
            val title: String = titleUpdate.text.toString()
            val author: String = authorUpdate.text.toString()
            val isbn: Long = isbnUpdate.text.toString().toLong()
            val genre: String = genreUpdate.text.toString()
            val notes: String = notesUpdate.text.toString()

            if(title.trim().isEmpty())
            {
                titleUpdate.error = "Required"
                titleUpdate.requestFocus()
                return
            }

            if(author.trim().isEmpty())
            {
                authorUpdate.error = "Required"
                authorUpdate.requestFocus()
                return
            }

            //getting user ID
            val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val id = bookId //id = bookId which was the id passed from MainActivity

            database.collection("userData")
                .document(userId) //using user ID to for unique users
                .collection("Books")
                .document(id)
                .set(Book(title, author, isbn, genre, notes), SetOptions.merge())
                .addOnCompleteListener {
                    Toast.makeText(this,
                        "Book updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,
                        "Book not updated", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
        catch(e: Exception)
        {
            isbnUpdate.error = "Required"
            isbnUpdate.requestFocus()
            return
        }

    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.saveInfo -> {
                updateBook()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


