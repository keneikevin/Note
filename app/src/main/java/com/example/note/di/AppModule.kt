package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.data.local.NoteDatabase
import com.example.note.other.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNotesDatabase(
        @ApplicationContext context:Context
    ) = Room.databaseBuilder(context,NoteDatabase::class.java, DATABASE_NAME).build()
}