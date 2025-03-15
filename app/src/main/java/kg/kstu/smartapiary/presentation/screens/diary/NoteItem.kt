package kg.kstu.smartapiary.presentation.screens.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.kstu.smartapiary.domain.data.Note

@Composable
fun NoteItem(
    note: Note,
    onEdit: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onToggleComplete: (Note) -> Unit,
    isCompletedStyle: Boolean = false
) {
    var showDialog by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(note.text) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(enabled = !isCompletedStyle) { showDialog = true }, // ✅ Блокируем редактирование завершенных заметок
        colors = CardDefaults.cardColors(containerColor = if (isCompletedStyle) Color(0xFFEAEAEA) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.padding(end = 8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
                        .background(Color(0xFFFBC803))
                        .clickable { onToggleComplete(note) },
                    contentAlignment = Alignment.Center
                ) {
                    if (note.isCompleted) {
                        Text("✓", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.text,
                    fontSize = 16.sp,
                    textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isCompletedStyle) Color.DarkGray else Color.Black
                )
                Text(
                    text = note.date,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = { onDelete(note) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Black
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Редактировать заметку") },
            text = {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEdit(note.copy(text = editedText)) // ✅ Передаем новый текст
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFBC803), // ✅ Желтый фон кнопки
                        contentColor = Color.Black // ✅ Черный цвет текста
                    ),
                    shape = RoundedCornerShape(8.dp) // ✅ Скругление 8dp
                ) {
                    Text("Сохранить", color = Color.Black) // ✅ Черный текст
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFBC803), // ✅ Желтый фон кнопки
                        contentColor = Color.Black // ✅ Черный цвет текста
                    ),
                    shape = RoundedCornerShape(8.dp) // ✅ Скругление 8dp
                ) {
                    Text("Отмена", color = Color.Black) // ✅ Черный текст
                }
            }
        )
    }

}

