package com.example.note.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locally_Deleted_NoteIds")
data class LocallyDeletedNoteId(
    @PrimaryKey(autoGenerate = false)
    val deletedNoteId: String
)
