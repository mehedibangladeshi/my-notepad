package com.greytechlab.mynotepad.ui

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.greytechlab.mynotepad.R
import com.greytechlab.mynotepad.data.db.NoteDatabase
import com.greytechlab.mynotepad.data.model.Note
import com.greytechlab.mynotepad.databinding.FragmentAddNoteBinding
import com.greytechlab.mynotepad.ui.base.BaseFragment
import com.greytechlab.mynotepad.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddNoteFragment: BaseFragment() {
    private var _binding : FragmentAddNoteBinding? = null
    // This property is only valid between onCreateView() and onDestroyView().
    private val binding get() = _binding!!
    private final val STORAGE_PERMISSION_CODE = 33

    private var mNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            mNote = AddNoteFragmentArgs.fromBundle(it).note
            if (mNote != null){
                binding.etTitle.setText(mNote?.title)
                binding.etNote.setText(mNote?.note)
                val activity = requireActivity() as AppCompatActivity
                activity.supportActionBar?.setTitle(R.string.label_edit_note)

                val noteCreated = "Note Created: "+ getTimeInTextFormat(mNote!!.dateTimeCreated)
                val noteUpdated = "Last Updated: "+ getTimeInTextFormat(mNote!!.dateTimeUpdated)
                binding.tvCreated.visibility = View.VISIBLE

                binding.tvCreated.text = noteCreated
                binding.tvUpdated.visibility = View.VISIBLE
                binding.tvUpdated.text = noteUpdated
            }
        }

        binding.btnSave.setOnClickListener{

            validateNote()
        }


    }

    private fun validateNote() {
        val titleText = binding.etTitle.text.toString().trim()
        val noteText = binding.etNote.text.toString().trim()

        if (titleText.isEmpty()){
            binding.etTitle.error = "Title Required"
            binding.etTitle.requestFocus()
            return
        }

        if (noteText.isEmpty()){
            binding.etNote.error = "Note Required"
            binding.etNote.requestFocus()
            return
        }

            setNoteDetails(titleText, noteText)
    }

    private fun setNoteDetails(titleText: String, noteText: String){
        launch {
            context?.let {

                if (mNote == null){
                    val note = Note(
                        titleText,
                        noteText,
                        getCurrentTimeInMillisString(),
                        getCurrentTimeInMillisString()
                    )
                    writeNoteToDB(note)
                    it.toast("Note Saved")
                }else{

                    val note = Note(
                        titleText,
                        noteText,
                        mNote!!.dateTimeCreated,
                        getCurrentTimeInMillisString()
                    )
                    note.id = mNote!!.id
                    updateNoteToDB(note)
                    it.toast("Note Updated")
                }


                navigateToHome()
            }
        }

    }

    private suspend fun writeNoteToDB(note: Note) {
        withContext(Dispatchers.IO){
            context?.let {
                NoteDatabase(it).getNoteDao().addNote(note)

            }
        }

    }

    private suspend fun updateNoteToDB(note: Note) {
        withContext(Dispatchers.IO){
            context?.let {
                NoteDatabase(it).getNoteDao().updateNote(note)

            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        // releasing binding class
        _binding = null
    }

    private fun deleteNoteDialog() {
        android.app.AlertDialog.Builder(context,
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        ).apply {

            setTitle("Delete?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Delete"){_, _ ->
                launch {

                    deleteNote(mNote!!)
                    context.toast("Note Deleted")
                    navigateToHome()
                }
            }
            setNegativeButton("Cancel"){_,_ ->
                context.toast("Canceled")
            }

        }.create().show()

    }

    private suspend fun deleteNote(note: Note) {
        NoteDatabase(requireContext()).getNoteDao().deleteNote(note)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> if (mNote != null) deleteNoteDialog() else context?.toast("Cannot delete the Note")
            R.id.exportToSd -> exportNote()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun exportNote() {
// check permission to write on external storage
        if (ContextCompat
                .checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED )
            {
            // Permission is granted
            requestForPermission()

        }else{
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()){
                val exportNote = Note(
                    binding.etTitle.text.toString().trim(),
                    binding.etNote.text.toString().trim(),
                    dateTimeCreated = if (mNote != null) mNote!!.dateTimeCreated else getCurrentTimeInMillisString(),
                    dateTimeUpdated = getCurrentTimeInMillisString()
                )

                showToast(exportToInternalDir(exportNote, this.requireContext()))
            }else {
                showToast(Environment.getExternalStorageState())
            }



        }

    }

    private fun requestForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission needed")
                .setMessage("Permission needed to export Note to External Storage.")
                .setPositiveButton("ok",
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .create().show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE);
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)
    }

    private fun navigateToHome() {
        val action = AddNoteFragmentDirections.actionSaveNote()
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    exportNote()
                }else{
                    showToast("Permission Denied")
                }
            }

        }



    }

    // Request code for creating a text file.
    private final val CREATE_FILE = 1

//    private fun createFile(note: Note) {
//        val pickerInitialUri: Uri = File()
//        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "text/plain"
//            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")
//
//            // Optionally, specify a URI for the directory that should be opened in
//            // the system file picker before your app creates the document.
//            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
//        }
//        startActivityForResult(intent, CREATE_FILE)
//    }

}