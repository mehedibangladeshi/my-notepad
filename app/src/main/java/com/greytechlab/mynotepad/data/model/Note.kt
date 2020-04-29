package com.greytechlab.mynotepad.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(

    //  uncomment next to have a different name for the "title" column
    //  @ColumnInfo(name = "NOTE_TITLE")
    val title: String,
    val note: String,

    val dateTimeCreated: String,
    var dateTimeUpdated: String
) :Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}