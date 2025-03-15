package kg.kstu.smartapiary.presentation.screens.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.kstu.smartapiary.domain.data.Note
import kg.kstu.smartapiary.domain.room.NoteDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {
    var notes by mutableStateOf(emptyList<Note>())
        private set

    init {
        loadNotes()
    }

    fun addNote(text: String) {
        val note = Note(
            id = UUID.randomUUID().toString(),
            text = text,
            date = getCurrentDate(),
            isCompleted = false,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            noteDao.insert(note)
            loadNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
            loadNotes()
        }
    }

    fun updateNote(note: Note, newText: String) {
        val updatedNote = note.copy(text = newText)
        viewModelScope.launch {
            noteDao.update(updatedNote)
            loadNotes()
        }
    }

    fun toggleNoteCompletion(note: Note) {
        val updatedNote = note.copy(isCompleted = !note.isCompleted)
        viewModelScope.launch {
            noteDao.update(updatedNote)
            loadNotes()
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            notes = noteDao.getAllNotes().sortedByDescending { it.timestamp }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
