package com.greytechlab.mynotepad.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.greytechlab.mynotepad.data.model.Note

class NoteViewModel(application: Application) : AndroidViewModel(application){

   private lateinit var noteList: LiveData<List<Note>>

}