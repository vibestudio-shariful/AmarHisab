
package com.amarhisab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amarhisab.ui.viewmodel.MainViewModel

@Composable
fun DuesScreen(viewModel: MainViewModel) {
    val persons by viewModel.persons.collectAsState()
    var showAddPersonDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("বাকি হিসাব", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(persons) { person ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(person.name, fontWeight = FontWeight.SemiBold)
                            Text("৳ 0.00", color = Color.Gray) // Replace with actual logic
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddPersonDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Person")
        }
    }

    if (showAddPersonDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddPersonDialog = false },
            title = { Text("নতুন ব্যক্তি যোগ করুন") },
            text = { TextField(value = name, onValueChange = { name = it }, label = { Text("ব্যক্তির নাম") }) },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isNotBlank()) viewModel.addPerson(name)
                    showAddPersonDialog = false
                }) { Text("যোগ করুন") }
            },
            dismissButton = { TextButton(onClick = { showAddPersonDialog = false }) { Text("বাতিল") } }
        )
    }
}
