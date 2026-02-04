
package com.amarhisab.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amarhisab.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val profile by viewModel.profile.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(100.dp).padding(8.dp)) {
            // Placeholder for Profile Photo
            Surface(modifier = Modifier.fillMaxSize(), shape = androidx.compose.foundation.shape.CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
                Box(contentAlignment = Alignment.Center) {
                    Text(profile?.name?.take(1)?.uppercase() ?: "?", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        
        Text(profile?.name ?: "ব্যবহারকারী", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(profile?.email ?: "ইমেইল যোগ করা হয়নি", color = androidx.compose.ui.graphics.Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("সিস্টেম", fontWeight = FontWeight.SemiBold)
                
                Button(onClick = { /* Backup Logic */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("ব্যাকআপ (JSON)")
                }
                
                OutlinedButton(onClick = { /* Restore Logic */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("রিস্টোর")
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Text("App Version 1.0.0", fontSize = 12.sp, color = androidx.compose.ui.graphics.Color.LightGray)
    }
}
