package com.twistentiger.startrak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class BookAdapter(options: FirestoreRecyclerOptions<Book>) : FirestoreRecyclerAdapter<Book, BookAdapter.BookHolder>(options)
{
    //private val books: MutableList<Book> = ArrayList()
    private lateinit var listener: OnItemClickListener

    @NonNull
    @Override
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BookHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.info_item, parent, false)
        return BookHolder(itemView)
    }

    @NonNull
    @Override
    override fun onBindViewHolder(@NonNull holder: BookHolder, position: Int, @NonNull model: Book)
    {
        holder.titleView.text = model.title
        holder.authorView.text = model.author
        holder.genreView.text = model.genre
        holder.isbnView.text = model.isbn.toString()
    }

    fun deleteItem(position: Int)
    {
        snapshots.getSnapshot(position).reference.delete()
    }

    inner class BookHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val titleView: TextView = itemView.findViewById(R.id.title_textView)
        val authorView: TextView = itemView.findViewById(R.id.author_textView)
        val genreView: TextView = itemView.findViewById(R.id.genre_textView)
        val isbnView: TextView = itemView.findViewById(R.id.isbn_textView)

        init {
            itemView.setOnClickListener(object: View.OnClickListener
            {
                override fun onClick(v: View?)
                {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position)
                    }
                }
            })
        }
    }

    interface OnItemClickListener
    {
        fun onItemClick(documentSnapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemClickListener(newListener: OnItemClickListener)
    {
        this.listener = newListener
    }
}