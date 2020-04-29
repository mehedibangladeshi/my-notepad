package com.greytechlab.mynotepad.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greytechlab.mynotepad.common.OnClickListener
import com.greytechlab.mynotepad.R
import com.greytechlab.mynotepad.data.model.Note
import com.greytechlab.mynotepad.utils.getTimeInTextFormat
import kotlinx.android.synthetic.main.note_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(var noteList: List<Note>):RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(), Filterable {

    private val noteListFull: List<Note>
    init {
        noteListFull = ArrayList<Note>(noteList)
    }

    private lateinit var _listener: OnClickListener
    public var listener: OnClickListener
    set(value) {
        _listener = value
    }
        get() = _listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(view)


    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = noteList[position]
        with(holder){
            tvTitle.text = note.title
            tvNote.text = note.note
            tvDateUpdate.text = getTimeInTextFormat(note.dateTimeUpdated)
        }

        holder.itemView.setOnClickListener {
            // filtering to get actual position of the note
            for (n in noteListFull){
                if (noteList[position].id == n.id)
                    listener.onItemClick(noteListFull.indexOf(n), holder.itemView)
            }
        }

    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.tvTitle
        val tvNote: TextView = itemView.tvNote
        val tvDateUpdate: TextView = itemView.tvUpdated
    }

    // filtering (search) notelist
     override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filterList = ArrayList<Note>()
                if (constraint.isNullOrEmpty()){

                    filterList.addAll(noteListFull)
                }
                else{
                    var filterPattern : String = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                    for (n in noteListFull){
                        if (n.title.toLowerCase(Locale.getDefault()).contains(filterPattern))
                        filterList.add(n)
                    }
                }
                val filterResult = FilterResults()
                 filterResult.values = filterList
                return filterResult
            }
            @SuppressWarnings()
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    noteList = results?.values as ArrayList<Note>
                    notifyDataSetChanged()
            }
        }
    }



}



