package com.twistentiger.startrak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookHolder>()
{
    private val books: MutableList<Book> = ArrayList()

    @NonNull
    @Override
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BookHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.info_item, parent, false)
        return BookHolder(itemView)
    }

    override fun getItemCount(): Int
    {
        return books.size
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int)
    {
        val currentBook = books.get(position)
        holder.titleView.setText(currentBook.title)
        holder.authorView.setText(currentBook.author)
        holder.genreView.setText(currentBook.genre)
        //holder.isbnView.setText(currentBook.bookISBN)
    }



    class BookHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val titleView: TextView = itemView.findViewById(R.id.title_textView)
        val authorView: TextView = itemView.findViewById(R.id.author_textView)
        val genreView: TextView = itemView.findViewById(R.id.genre_textView)
        val isbnView: TextView = itemView.findViewById(R.id.isbn_textView)
    }
}