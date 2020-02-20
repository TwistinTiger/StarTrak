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
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import com.getbase.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity()
{
    //getting user ID to display their individual data
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val bookStorageRef: CollectionReference = database
        .collection("userData")
        .document(userId)
        .collection("Books")

    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //prompts the adding of books
        val fabAddingBook: FloatingActionButton = findViewById(R.id.fab_action1)
        fabAddingBook.setOnClickListener{
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

        /**
         * Need an easier way to add books to database by scanning ISBN or ML scan book
         * Might need to open a browser app and get the data from the internet that way we don't type the whole book
         * TODO(Activity to scan book)
         */
        val fab2: FloatingActionButton = findViewById(R.id.fab_action2)
        fab2.setOnClickListener{
            showToast("Up coming feature")
        }

        setUpRecyclerView()
    }

    private fun showToast(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        recyclerView.layoutManager = GridLayoutManager(this,2)
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
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
            {
                val book = documentSnapshot.toObject(Book::class.java)
                val id: String = documentSnapshot.id
                val actualPosition = position + 1
                val updateIntent = Intent(this@MainActivity,
                    UpdateActivity::class.java) // update that starts the update phase of app

                updateIntent.putExtra("book", book) // passing book object through intent
                updateIntent.putExtra("bookId", id) //passing book id through intent

                this@MainActivity.startActivity(updateIntent) // starting activity

                Toast.makeText(this@MainActivity,
                    "Position: $actualPosition, ID: $id", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logOut()
    {
        val logoutIntent = Intent(this@MainActivity, SignInActivity::class.java)
        this@MainActivity.startActivity(logoutIntent)
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

    @Override
    override fun onBackPressed()
    {
        val intentWhenBackPressed = Intent(this@MainActivity, SignInActivity::class.java)
        intentWhenBackPressed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //clearing flags to sort issues or security
        this@MainActivity.startActivity(intentWhenBackPressed)
        super.onBackPressed()
    }
}

