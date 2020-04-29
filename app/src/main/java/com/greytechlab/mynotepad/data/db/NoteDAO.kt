package com.greytechlab.mynotepad.data.db

import androidx.room.*
import com.greytechlab.mynotepad.data.model.Note

@Dao
interface NoteDAO {
    @Insert
    suspend fun addNote(note: Note)

    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY dateTimeUpdated DESC")
    suspend fun getAllNotesByDateUpdateDESC():List<Note>

    @Query("SELECT * FROM note ORDER BY dateTimeCreated DESC")
    suspend fun getAllNotesByDateCreateDESC():List<Note>

    @Query("SELECT * FROM note ORDER BY title DESC")
    suspend fun getAllNotesByTitleDESC():List<Note>

    @Query("SELECT * FROM note ORDER BY dateTimeUpdated")
    suspend fun getAllNotesByDateUpdateACEN():List<Note>

    @Query("SELECT * FROM note ORDER BY dateTimeCreated")
    suspend fun getAllNotesByDateCreateACEN():List<Note>

    @Query("SELECT * FROM note ORDER BY title")
    suspend fun getAllNotesByTitleACEN():List<Note>
//    suspend fun getAllNotes():LiveData<List<Note>>


}  