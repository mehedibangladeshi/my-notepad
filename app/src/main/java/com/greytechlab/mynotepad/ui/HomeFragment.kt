package com.greytechlab.mynotepad.ui

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.greytechlab.mynotepad.*
import com.greytechlab.mynotepad.common.OnClickListener
import com.greytechlab.mynotepad.common.OnDialogListItemClickListener
import com.greytechlab.mynotepad.data.db.NoteDatabase
import com.greytechlab.mynotepad.data.model.Note
import com.greytechlab.mynotepad.databinding.FragmentHomeBinding
import com.greytechlab.mynotepad.ui.adapter.NotesAdapter
import com.greytechlab.mynotepad.ui.base.BaseFragment
import com.greytechlab.mynotepad.ui.dialog.ShowSingleChoiceItemListDialog
import com.greytechlab.mynotepad.utils.sortingOptions
import kotlinx.coroutines.launch

class HomeFragment: BaseFragment() {

    // This property is only valid between onCreateView() and onDestroyView().

    private val binding get() = _binding!!
    private var _binding : FragmentHomeBinding? = null
    lateinit var noteList: List<Note>
    lateinit var notesAdapter: NotesAdapter

//    private lateinit var mNavController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mNavController = Navigation.findNavController(view)


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        loadAllNotes(getFilterMode())
    }

    private fun initListeners() {


        binding.btnAdd.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeToAddNote()
            Navigation.findNavController(requireView()).navigate(action)
        }

        notesAdapter.listener = object :
            OnClickListener {
            override fun onItemClick(position: Int, view: View) {
                val action =
                    HomeFragmentDirections.actionHomeToAddNote()
                action.note = noteList[position]
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    private fun loadAllNotes(filterMode: Int) {

        binding.rvNotes.invalidateItemDecorations()
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {

                noteList = when(filterMode){
                    0 ->  NoteDatabase(it).getNoteDao().getAllNotesByDateUpdateDESC()
                    1 ->  NoteDatabase(it).getNoteDao().getAllNotesByDateUpdateACEN()
                    2 ->  NoteDatabase(it).getNoteDao().getAllNotesByDateCreateDESC()
                    3 ->  NoteDatabase(it).getNoteDao().getAllNotesByDateCreateACEN()
                    4 ->  NoteDatabase(it).getNoteDao().getAllNotesByTitleDESC()
                    5 ->  NoteDatabase(it).getNoteDao().getAllNotesByTitleACEN()

                    else -> NoteDatabase(it).getNoteDao().getAllNotesByDateUpdateDESC()
                }
                 notesAdapter = NotesAdapter(noteList)
                notesAdapter.notifyDataSetChanged()
                binding.rvNotes.adapter = notesAdapter


                initListeners()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        // releasing binding class
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
//            R.id.search ->  context?.toast("Search note")
            R.id.sort ->  showSortingFilter()
        }
        return super.onOptionsItemSelected(item)

    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
        val searchItem = menu.findItem(R.id.search)
         val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.filter.filter(newText)
                return false
            }
        })




    }

    private fun showSortingFilter() {
        val sortingListDialog =
            ShowSingleChoiceItemListDialog(
                "Sort By",
                sortingOptions(),
                getFilterMode()
            )
            
        sortingListDialog.setOnItemClickListener(object :
            OnDialogListItemClickListener {
            override fun onItemListener(position: Int) {

                setFilterMode(position)
                loadAllNotes(getFilterMode())
                sortingListDialog.dismiss()

            }
        })

        sortingListDialog.show(childFragmentManager,"sortByDialog")
        
    }



}