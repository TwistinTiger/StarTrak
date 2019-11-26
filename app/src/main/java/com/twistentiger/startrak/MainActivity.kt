package com.twistentiger.startrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*

/*
import com.twistentiger.startrak.SecondActivity.Companion.EXTRA_AUTHOR
import com.twistentiger.startrak.SecondActivity.Companion.EXTRA_GENRE
import com.twistentiger.startrak.SecondActivity.Companion.EXTRA_ISBN
import com.twistentiger.startrak.SecondActivity.Companion.EXTRA_TITLE
*/

class MainActivity : AppCompatActivity()
{
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val bookStorageRef: CollectionReference = database.collection("The Book")

    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)

        //prompts the adding of books
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView()
    {
        val query: Query = bookStorageRef.orderBy("title", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Book>()
            .setQuery(query, Book::class.java)
            .build()

        adapter = BookAdapter(options)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)

        //for now we use linear layout
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        {
            @Override
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean
            {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
                adapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(recyclerView)

        adapter.setOnItemClickListener(object: BookAdapter.OnItemClickListener
        {

            /**
             * For this, I need to implement an edit data algorithm
             * The loading to recylcerview algorithm might be better
             * Work on this tomorrow
             *
             * Goodnight Sir Robin
             */

            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
            {
                val book = documentSnapshot.toObject(Book::class.java)
                val id: String = documentSnapshot.id
                //val path: String = documentSnapshot.reference.path
                val actualPosition = position + 1

                Toast.makeText(this@MainActivity,
                    "Position: $actualPosition, ID: $id", Toast.LENGTH_SHORT).show()
            }

            /**
             * TODO("We still need an edit function")
             */
            /*override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
            {
                val editIntent = Intent(this@MainActivity, SecondActivity::class.java)
                val book = documentSnapshot.toObject(Book::class.java)

                if (book != null)
                {
                    editIntent.putExtra(EXTRA_TITLE, book.title)
                    editIntent.putExtra(EXTRA_AUTHOR, book.author)
                    editIntent.putExtra(EXTRA_ISBN, book.isbn)
                    editIntent.putExtra(EXTRA_GENRE,book.genre)
                    startActivityForResult(editIntent, )
                }
                //val id: String = documentSnapshot.id
                //val path: String = documentSnapshot.reference.path
                //val actualPosition = position + 1

                //Toast.makeText(this@MainActivity,
                    //"Position: $actualPosition, ID: $id", Toast.LENGTH_SHORT).show()
            }*/
        })
    }

    /**
     * TODO("Implement a logout Activity")
     */
    private fun logOut()
    {
        Toast.makeText(this,
            "Logout clicked", Toast.LENGTH_SHORT).show()
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.delete_all_books -> {
               logOut()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Override
    override fun onStart()
    {
        super.onStart()
        adapter.startListening()
    }

    @Override
    override fun onStop()
    {
        super.onStop()
        adapter.stopListening()
    }
}

