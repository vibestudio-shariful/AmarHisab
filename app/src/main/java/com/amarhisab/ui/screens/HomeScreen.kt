
package com.amarhisab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amarhisab.R
import com.amarhisab.data.Transaction
import com.amarhisab.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val balance by viewModel.balance.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("আমার হিসাব", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            item {
                SummaryCard(balance, totalIncome, totalExpense)
            }

            item {
                Text("সাম্প্রতিক লেনদেন", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            }

            items(transactions.take(10)) { tx ->
                TransactionItem(tx)
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add",强化Color.White)
        }
    }

    if (showAddDialog) {
        AddTransactionDialog(onDismiss = { showAddDialog = false }, onConfirm = { type, amount, category, note ->
            viewModel.addTransaction(type, amount, category, note)
            showAddDialog = false
        })
    }
}

@Composable
fun SummaryCard(balance: Double, income: Double, expense: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("বর্তমান ব্যালেন্স", color = Color.White.copy(alpha = 0.8f))
            Text("৳ ${String.format("%.2f", balance)}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("মোট আয়", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("৳ ${String.format("%.2f", income)}", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("মোট ব্যয়", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("৳ ${String.format("%.2f", expense)}", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(tx: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = if (tx.type == "INCOME") Color(0xFF4CAF50) else Color(0xFFE53935)
            Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(6.dp)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(tx.category, fontWeight = FontWeight.Bold)
                Text(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(tx.date)), fontSize = 12.sp, color = Color.Gray)
            }
            Text(
                "${if (tx.type == "INCOME") "+" else "-"} ৳ ${tx.amount}",
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onConfirm: (String, Double, String, String) -> Unit) {
    var type by remember { mutableStateOf("INCOME") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("নতুন হিসাব যোগ করুন") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { type = "INCOME" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (type == "INCOME") Color(0xFF4CAF50) else Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) { Text("আয়") }
                    Button(
                        onClick = { type = "EXPENSE" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (type == "EXPENSE") Color(0xFFE53935) else Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) { Text("ব্যয়") }
                }
                TextField(value = amount, onValueChange = { amount = it }, label = { Text("পরিমাণ (টাকা)") }, modifier = Modifier.fillMaxWidth())
                TextField(value = category, onValueChange = { category = it }, label = { Text("ক্যাটাগরি") }, modifier = Modifier.fillMaxWidth())
                TextField(value = note, onValueChange = { note = it }, label = { Text("নোট (ঐচ্ছিক)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (amt > 0) onConfirm(type, amt, category, note) 
            }) { Text("সংরক্ষণ") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("বাতিল") } }
    )
}
