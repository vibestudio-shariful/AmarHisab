
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavingsScreen(viewModel: MainViewModel) {
    val savingsEntries by viewModel.savings.collectAsState()
    val totalSavings = savingsEntries.sumOf { it.amount }
    
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("সঞ্চয় / মূলধন", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFB8C00))
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("মোট সঞ্চয়", color = Color.White.copy(alpha = 0.8f))
                    Text("৳ ${String.format("%.2f", totalSavings)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text("লেনদেনের ইতিহাস", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(savingsEntries) { entry ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(entry.note)
                                Text(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(entry.date)), fontSize = 12.sp, color = Color.Gray)
                            }
                            Text("৳ ${entry.amount}", fontWeight = FontWeight.Bold, color = if (entry.amount >= 0) Color(0xFF4CAF50) else Color(0xFFE53935))
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = Color(0xFFFB8C00)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Savings", tint = Color.White)
        }
    }

    if (showAddDialog) {
        var amount by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("সঞ্চয় যোগ / হ্রাস করুন") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(value = amount, onValueChange = { amount = it }, label = { Text("পরিমাণ (+ জমা, - খরচ)") })
                    TextField(value = note, onValueChange = { note = it }, label = { Text("নোট") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    if (amt != 0.0) viewModel.updateSavings(amt, note)
                    showAddDialog = false
                }) { Text("ঠিক আছে") }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("বাতিল") } }
        )
    }
}
