package com.example.note.repository

import android.content.Context
import com.example.note.data.local.NoteDao
import com.example.note.data.remote.NoteApi
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val context: Context,
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
) {
}