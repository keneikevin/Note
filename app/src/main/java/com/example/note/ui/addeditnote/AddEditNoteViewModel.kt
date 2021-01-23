package com.example.note.ui.addeditnote
import androidx.lifecycle.viewModelScope
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.note.data.local.entities.Note
import com.example.note.other.Event
import com.example.note.other.Resource
import com.example.note.repository.NoteRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditNoteViewModel @ViewModelInject constructor(
    private val repository: NoteRepository) :ViewModel(){
    private val _note = MutableLiveData<Event<Resource<Note>>>()
    val note: LiveData<Event<Resource<Note>>> = _note
    fun insertNote(note: Note) = GlobalScope.launch {
        repository.insertNote(note)
    }

    fun getNoteById(noteId:String) = viewModelScope.launch {
        _note.postValue(Event(Resource.loading(null)))
        val note = repository.getNoteById(noteId)
        note?.let {
            _note.postValue(Event(Resource.success(it)))
        }?:_note.postValue(Event(Resource.error("Note not found", null)))
    }
    }