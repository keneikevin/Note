package com.example.note.ui.notes

import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.ui.BaseFragment

class NotesFragment:BaseFragment(R.layout.fragment_notes) {
    private val viewModel :NotesViewModel by viewModels()
}