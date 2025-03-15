package kg.kstu.smartapiary.presentation.screens.diary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.kstu.smartapiary.domain.data.Note
import kg.kstu.smartapiary.presentation.screens.viewmodel.DiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(viewModel: DiaryViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    var noteText by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Дневник") })
        },
        bottomBar = {
            WriteTaskBar(
                noteText = noteText,
                onTextChange = { noteText = it },
                onAddClick = {
                    if (noteText.isNotBlank()) {
                        if (editingNote == null) {
                            viewModel.addNote(noteText)
                        } else {
                            viewModel.updateNote(editingNote!!, noteText)
                            editingNote = null
                        }
                        noteText = ""
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val completedNotes = viewModel.notes.filter { it.isCompleted }
            val activeNotes = viewModel.notes.filter { !it.isCompleted }.sortedByDescending { it.date }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(activeNotes) { note ->
                    NoteItem(
                        note = note,
                        onEdit = { viewModel.updateNote(it, it.text) },
                        onDelete = { viewModel.deleteNote(it) },
                        onToggleComplete = { viewModel.toggleNoteCompletion(it) }
                    )
                }


                if (completedNotes.isNotEmpty()) {
                    item {
                        Text(
                            text = "ВЫПОЛНЕНО",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(completedNotes) { note ->
                        NoteItem(
                            note = note,
                            onEdit = { },
                            onDelete = { viewModel.deleteNote(it) },
                            onToggleComplete = { viewModel.toggleNoteCompletion(it) },
                            isCompletedStyle = true
                        )
                    }
                }
            }
        }
    }
}