package com.example.note.ui.notedetail

import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesDetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    private val viewModel : NotesDetailFragment by viewModels()
}