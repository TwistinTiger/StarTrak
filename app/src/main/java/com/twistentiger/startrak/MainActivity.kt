package com.twistentiger.startrak

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity()
{
    /**
     * adopted from previous videos, request the data from second activity*/
    companion object{
        const val ADD_BOOK_REQUEST: Int = 1
    }

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)

        //prompts the adding of books
        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            this@MainActivity.startActivity(intent)
        })


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        val adapter = BookAdapter()
        recyclerView.adapter = adapter

    }

    /**
     * Add the onActivityResult override method here.
     * Same as for this method.  got from android architecture components video.
     */
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_BOOK_REQUEST && resultCode == Activity.RESULT_OK)
        {
            val title = data!!.getStringExtra(SecondActivity.EXTRA_TITLE)
            val author = data.getStringExtra(SecondActivity.EXTRA_AUTHOR)
            val isbn = data.getLongExtra(SecondActivity.EXTRA_ISBN,0)
            val genre = data.getStringExtra(SecondActivity.EXTRA_GENRE)

            val book = Book(title, author, isbn, genre)
        }

    }*/

    /**
     * Come back later to the video Firestore Tutorial by coding in Flow Part 8
     * the time is around 2:59.
     * Add the the success listeners and failure listeners for the addNote() method
     * for you it is addBook(view: View) after notebookRef.add(note) --> bookRef.add(book)
     *
     *You might need the previous automatic load of data to app
     * Just come back and rewatch the video
     */
}

