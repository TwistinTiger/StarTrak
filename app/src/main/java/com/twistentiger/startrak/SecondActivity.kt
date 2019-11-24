package com.twistentiger.startrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.*

class SecondActivity : AppCompatActivity()
{
    /**
     * Added for result send back to main activity into the recyclerview
     */
    companion object{
        const val TAG = "SecondActivity"
        const val EXTRA_TITLE: String = "com.twistentiger.startrak.EXTRA_TITLE"
        const val EXTRA_AUTHOR: String = "com.twistentiger.startrak.EXTRA_AUTHOR"
        const val EXTRA_ISBN: String = "com.twistentiger.startrak.EXTRA_ISBN"
        const val EXTRA_GENRE: String = "com.twistentiger.startrak.EXTRA_GENRE"

    }

    private lateinit var titleEdit: TextInputEditText
    private lateinit var authorEdit: TextInputEditText
    private lateinit var isbnEdit: TextInputEditText
    private lateinit var genreEdit: TextInputEditText
    private lateinit var dataTextView: TextView

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val bookStorageRef: CollectionReference = database.collection("The Book")
    private val bookReference: DocumentReference = database.document("The Book/First Book")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        titleEdit = findViewById(R.id.title_editText)
        authorEdit = findViewById(R.id.author_editText)
        isbnEdit = findViewById(R.id.isbn_editText)
        genreEdit = findViewById(R.id.genre_editText)
        dataTextView = findViewById(R.id.text_view_data)
    }

    //preventing the use of bandwidth when the app is in the background
    @Override
    override fun onStart()
    {
        super.onStart()
        bookStorageRef.orderBy("title", Query.Direction.ASCENDING) //next time app opens it auto orders
            .addSnapshotListener(this, object: EventListener<QuerySnapshot>
        {
            override fun onEvent(queryDocumentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?)
            {
                if(e != null)
                {
                    return
                }

                if (queryDocumentSnapshots != null)
                {
                    var data = ""
                    for(documentSnapshot: QueryDocumentSnapshot in queryDocumentSnapshots)
                    {
                        val book: Book = documentSnapshot.toObject(Book::class.java)
                        book.setId(documentSnapshot.id)

                        val id: String = book.getId()
                        val title: String = book.title
                        val author: String = book.author
                        val isbn: Long = book.isbn
                        val genre:String = book.genre

                        data += "ID: $id \nTitle: $title \nAuthor: $author \nISBN: " +
                                "$isbn \nGenre: $genre \n\n"
                    }
                    dataTextView.text = data
                }
            }
        })
    }


    //this will go in the option selected override method
    private fun addBook()
    {
        val title: String = titleEdit.text.toString()
        val author: String = authorEdit.text.toString()
        val isbn: Long = isbnEdit.text.toString().toLong()
        val genre: String = genreEdit.text.toString()

        if(TextUtils.isEmpty(title))
        {
            titleEdit.error = "Required"
            return
        }

        if(TextUtils.isEmpty(author))
        {
            authorEdit.error = "Required"
            return
        }

        //try find solution to this

        /**
         *This is where we left off. come back here and we'll work on it.
         * Enjoy you game sir Robin.
         */
        /*if(isbn )
        {
            isbnEdit.error = "Required"
            return
        }*/

        val book = Book(title, author, isbn, genre)

        bookStorageRef.add(book).addOnSuccessListener {
            Toast.makeText(this@SecondActivity,
                "Command Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {e ->
            Toast.makeText(this@SecondActivity,
                "Error!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, e.toString());
        }
    }

    fun loadBooks(view: View)
    {
        bookStorageRef.orderBy("title", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->

                if (queryDocumentSnapshots != null)
                {
                    var data = ""
                    for(documentSnapshot: QueryDocumentSnapshot in queryDocumentSnapshots)
                    {
                        val book: Book = documentSnapshot.toObject(Book::class.java)
                        book.setId(documentSnapshot.id)

                        val id: String = book.getId()
                        val title: String = book.title
                        val author: String = book.author
                        val isbn: Long = book.isbn
                        val genre:String = book.genre

                        data += "ID: $id \nTitle: $title \nAuthor: $author \nISBN: " +
                                "$isbn \nGenre: $genre \n\n"
                    }
                    dataTextView.text = data
                }
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
                addBook()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
