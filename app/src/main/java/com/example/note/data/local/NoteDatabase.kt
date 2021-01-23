package com.example.note.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.note.data.local.entities.LocallyDeletedNoteId
import com.example.note.data.local.entities.Note

@Database(
    entities = [Note::class, LocallyDeletedNoteId::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NoteDatabase: RoomDatabase(){
    abstract fun noteDao(): NoteDao

}
