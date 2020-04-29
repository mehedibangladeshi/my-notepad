package com.greytechlab.mynotepad.data

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class NoteRepository(application: Application): CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
//
//    private var noteDAO: NoteDAO?
//    private lateinit var noteLiveData: MutableLiveData<List<Note>>
//
//    init {
//        noteDAO = NoteDatabase(application).getNoteDao()
//    }
//
//     fun getAllNotes() {
//            launch {
//     noteLiveData.value = noteDAO?.getAllNotes()
//        }
//    }
//    fun deleteNote(note: Note){
//        launch {
//            noteDAO?.deleteNote(note)
//        }
//    }
//
//    fun updateNote(note: Note){
//        launch {
//            noteDAO?.updateNote(note)
//        }
//    }
//    fun addNote(note: Note){
//        launch {
//            noteDAO?.addNote(note)
//        }
//    }
//


}