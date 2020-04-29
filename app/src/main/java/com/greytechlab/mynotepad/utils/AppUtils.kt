package com.greytechlab.mynotepad.utils

import android.content.Context
import android.icu.util.UniversalTimeScale.toLong
import android.os.Environment
import android.widget.Toast
import com.greytechlab.mynotepad.data.model.Note
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTimeInMillisString(): String = Calendar.getInstance().timeInMillis.toString()

fun getTimeInTextFormat(timeInMillis: String):String{
    val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss aaa")
    val date = Date(timeInMillis.toLong())
    return dateFormat.format(date)
}


fun Context.toast(text:String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

/*
* this function allows to write data to internal dir.
* it can only be possible to access via this app
*
* */
fun exportToInternalDir(note: Note, context: Context): String {
    val filepath = "my_notepad_backup"
    val fileName = note.title+".txt"
    var status:String = ""
//    var myExternalFile = File(context. (filepath),fileName)
    val fileOutPutStream: FileOutputStream

    try {
        fileOutPutStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutPutStream.write(note.note.toByteArray())
        status = "Saved to \"${context.filesDir}\""

    } catch (e: IOException) {
        e.printStackTrace()
        status = "Failed: ${e.localizedMessage}"
    }finally {
//        fileOutPutStream.close()
        return status
    }




}
fun exportToExternalDir(note: Note, context: Context): String {
    val filepath = "my_notepad_backup"
    val fileName = note.title + ".txt"
    var status = ""
//    var myExternalFile = File(context.filesDir(filepath), fileName)
    var myExternalFile = File(Environment.getRootDirectory(), fileName)
    val fileOutPutStream: FileOutputStream?
    fileOutPutStream = FileOutputStream(myExternalFile)
    status = try {
        fileOutPutStream.write(note.note.toByteArray())
        "Saved on External"
    } catch (e: IOException) {
        e.printStackTrace()
        "Failed: ${e.localizedMessage}"
    } finally {
        fileOutPutStream.close()
        return status
    }




}