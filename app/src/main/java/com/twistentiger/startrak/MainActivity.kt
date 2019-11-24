package com.twistentiger.startrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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
        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            this@MainActivity.startActivity(intent)
        })

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
            override fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
            {
                val book = documentSnapshot.toObject(Book::class.java)
                val id: String = documentSnapshot.id
                val path: String = documentSnapshot.reference.path
                Toast.makeText(this@MainActivity,
                    "Position: $position, ID: $id", Toast.LENGTH_SHORT).show()
            }
        })
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

