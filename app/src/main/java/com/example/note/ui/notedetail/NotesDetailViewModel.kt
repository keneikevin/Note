package com.example.note.ui.notedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.note.repository.NoteRepository

class NotesDetailViewModel @ViewModelInject constructor(
    repository: NoteRepository
):ViewModel() {
}