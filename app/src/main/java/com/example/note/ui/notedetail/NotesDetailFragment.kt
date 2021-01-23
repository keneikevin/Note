package com.example.note.ui.notedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.note.R
import com.example.note.data.local.entities.Note
import com.example.note.databinding.FragmentNoteDetailBinding
import com.example.note.other.Constants.ADD_OWNER_DIALOGUE_TAG
import com.example.note.other.Status
import com.example.note.ui.BaseFragment
import com.example.note.ui.dialogs.AddOwnerDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.fragment_note_detail.*
import kotlinx.android.synthetic.main.fragment_notes.*

@AndroidEntryPoint
class NotesDetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    private val viewModel: NotesDetailViewModel by viewModels()
    private lateinit var binding: FragmentNoteDetailBinding
    private val args: NotesDetailFragmentArgs by navArgs()
    private var curNote: Note? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteDetailBinding.bind(view)
        subscribeToObservers()
        binding.fabEditNote.setOnClickListener {
            findNavController().navigate(
                    NotesDetailFragmentDirections.actionNotesDetailFragmentToAddEditNoteFragment(args.id)
            )
        }

        if (savedInstanceState != null){
            val addOwnerDialog = parentFragmentManager.findFragmentByTag(ADD_OWNER_DIALOGUE_TAG)
            as AddOwnerDialog?
            addOwnerDialog?.setPositiveListener{
                addOwnerToCurNote(it)
            }
        }
    }

    private fun addOwnerToCurNote(email:String){
        curNote?.let { note ->  
            viewModel.addOwnerToNote(email, note.id)
        }
    }
    private fun showAddOwnerDialog(){
        AddOwnerDialog().apply {
            setPositiveListener {
                addOwnerToCurNote(it)
            }
        }.show(parentFragmentManager, ADD_OWNER_DIALOGUE_TAG)
    }

    private fun setMarkDownText(text: String){
        val markwon = Markwon.create(requireContext())
        val markdown= markwon.toMarkdown(text)
        markwon.setParsedMarkdown(binding.tvNoteContent, markdown)
    }
    private fun subscribeToObservers(){
        viewModel.observeNoteByID(args.id).observe(viewLifecycleOwner, Observer {
            it?.let { note ->
                binding.tvNoteTitle.text = note.title
                setMarkDownText(note.content)
                curNote = note
            }?:showSnackbar("Note not found")
        })
        viewModel.addOwnerStatus.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when(result.status){
                    Status.SUCCESS -> {
                    binding.addOwnerProgressBar.visibility = View.GONE
                        showSnackbar(result.data ?:"Successfully added owner to note")
                    }
                    Status.ERROR -> {
                    binding.addOwnerProgressBar.visibility = View.GONE
                        showSnackbar(result.message ?:"An unKnown error occurred")
                    }
                    Status.LOADING ->{
                        binding.addOwnerProgressBar.visibility = View.VISIBLE
                    }

            }

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_detail_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAddOwner -> showAddOwnerDialog()
        }
        return super.onOptionsItemSelected(item)
    }
}
