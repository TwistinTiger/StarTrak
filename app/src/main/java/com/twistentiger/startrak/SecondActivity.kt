package com.twistentiger.startrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.*

class SecondActivity : AppCompatActivity()
{
    //trying to see what can be broken
    /*companion object{
        const val EXTRA_TITLE: String = "com.twistentiger.startrak.EXTRA_TITLE"
        const val EXTRA_AUTHOR: String = "com.twistentiger.startrak.EXTRA_AUTHOR"
        const val EXTRA_ISBN: String = "com.twistentiger.startrak.EXTRA_ISBN"
        const val EXTRA_GENRE: String = "com.twistentiger.startrak.EXTRA_GENRE"
    }*/

    private lateinit var titleEdit: TextInputEditText
    private lateinit var authorEdit: TextInputEditText
    private lateinit var isbnEdit: TextInputEditText
    private lateinit var genreEdit: TextInputEditText
    private lateinit var notesEdit: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //for now I prefer the back arrow for close
        //supportActionBar.setHomeAsUpIndicator(R.drawable.ic_close)
        title = "Add Book"

        titleEdit = findViewById(R.id.title_editText)
        authorEdit = findViewById(R.id.author_editText)
        isbnEdit = findViewById(R.id.isbn_editText)
        genreEdit = findViewById(R.id.genre_editText)
        notesEdit = findViewById(R.id.notes_editText)
    }

    /**
     * This method saves the book to the database
     * Sends the results back to the MainActivity into recyclerview
     */
    private fun saveBook()
    {
        //Might add a try catch error
        val title: String = titleEdit.text.toString()
        val author: String = authorEdit.text.toString()
        val isbn: Long = isbnEdit.text.toString().toLong()
        val genre: String = genreEdit.text.toString()
        val notes: String = notesEdit.text.toString()

        if(title.trim().isEmpty())
        {
            titleEdit.error = "Required"
            return
        }

        if(author.trim().isEmpty())
        {
            authorEdit.error = "Required"
            return
        }

        //try find solution to this

        /**
         *This is where we left off. come back here and we'll work on it.
         * Enjoy you game sir Robin.
         */
        /*if(isbn.trim().isEmpty() )
        {
            isbnEdit.error = "Required"
            return
        }*/

        val bookReference: CollectionReference = FirebaseFirestore.getInstance()
            .collection("The Book")
        bookReference.add(Book(title, author, isbn, genre, notes))
        Toast.makeText(this, "Book saved", Toast.LENGTH_SHORT).show()

        finish()
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
                saveBook()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


