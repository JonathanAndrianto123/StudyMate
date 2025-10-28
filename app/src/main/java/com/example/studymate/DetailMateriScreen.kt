package com.example.studymate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground=true)
@Composable
fun DetailMateriScreen(materi: Materi = Materi("Pemrograman Android", "2 jam", 75)) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(materi.nama, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF24493E))
        Text("Durasi: ${materi.durasi}", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Progress", fontWeight = FontWeight.Medium)
        LinearProgressIndicator(
            progress = materi.progress / 100f,
            color = Color(0xFF1A3630),
            modifier = Modifier.fillMaxWidth().height(10.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* nanti: tandai selesai, edit, dll */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3630)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mulai Belajar", color = Color.White)
        }
    }
}
