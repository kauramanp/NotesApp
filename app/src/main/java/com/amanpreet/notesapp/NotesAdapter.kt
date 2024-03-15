package com.amanpreet.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(var list: ArrayList<Notes>,var notesClick: NotesClick)  : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var tvTitle : TextView = view.findViewById(R.id.tvTitle)
        var tvDescription : TextView = view.findViewById(R.id.tvDescription)
        var btnEdit : Button = view.findViewById(R.id.btnEdit)
        var btnDelete : Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.setText(list[position].title)
        holder.tvDescription.setText(list[position].description)
        holder.btnEdit.setOnClickListener {
            notesClick.editClick(list[position])
        }
        holder.btnDelete.setOnClickListener {
            notesClick.deleteClick(list[position])
        }
    }
}