package com.example.note.ui.addeditnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.note.R
import com.example.note.data.local.entities.Note
import com.example.note.databinding.FragmentAddEditNoteBinding
import com.example.note.other.Constants.DEFAULT_NOTE_COLOR
import com.example.note.other.Constants.FRAGMENT_TAG
import com.example.note.other.Constants.KEY_LOGGED_IN_EMAIL
import com.example.note.other.Constants.NO_EMAIL
import com.example.note.other.Status
import com.example.note.ui.BaseFragment
import com.example.note.ui.dialogs.ColorPickerDialogFragment
import com.skydoves.colorpickerview.ColorPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.item_note.*
import kotlinx.android.synthetic.main.item_note.viewNoteColor
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddEditNoteFragment:BaseFragment(R.layout.fragment_add_edit_note) {
    private val viewModel :AddEditNoteViewModel by viewModels()
    private val args: AddEditNoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentAddEditNoteBinding
    private var curNote: Note? = null
    private var curNoteColor = DEFAULT_NOTE_COLOR
    @Inject
    lateinit var sharedPref: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditNoteBinding.bind(view)
        if (args.id.isNotEmpty()){
            viewModel.getNoteById(args.id)
            subscribeToObservers()
        }
        if (savedInstanceState != null){
            val colorPickerDialog = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG)
            as ColorPickerDialogFragment?
            colorPickerDialog?.setPositiveListener {
                changeViewNoteColor(it)
            }
        }
            binding.viewNoteColor.setOnClickListener{
                ColorPickerDialogFragment().apply {
                    setPositiveListener {
                        changeViewNoteColor(it)
                    }
                }.show(parentFragmentManager, FRAGMENT_TAG)
            }


    }
    private fun changeViewNoteColor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#$colorString")
            DrawableCompat.setTint(wrappedDrawable, color)
            viewNoteColor.background = wrappedDrawable
            curNoteColor = colorString
        }
    }
    private fun subscribeToObservers(){
        viewModel.note.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when(result.status){
                    Status.SUCCESS -> {
                        val note = result.data!!
                        binding.etNoteTitle.setText(note.title)
                            binding.etNoteContent.setText(note.content)
                              changeViewNoteColor(note.color)
                    }
                    Status.LOADING -> {
                        /*NO_OP*/
                    }
                    Status.ERROR -> {
                        showSnackbar(result.message ?:"Note not found")
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }
    private fun saveNote(){
            val authEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
            val title = binding.etNoteTitle.text.toString()
            val content = binding.etNoteContent.text.toString()
            if (title.isEmpty() || content.isEmpty()){
                return
            }
            val date = System.currentTimeMillis()
            val color = curNoteColor
            val id = curNote?.id?:UUID.randomUUID().toString()
            val owners = curNote?.owners ?: listOf(authEmail)
            val note = Note(title,content,date,owners,color, id = id)
        viewModel.insertNote(note)
    }
}















































