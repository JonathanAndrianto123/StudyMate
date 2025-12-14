package com.example.studymate.uiux.materi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailMateriScreen(
    onStartTimer: () -> Unit,
    onProfileClick: () -> Unit = {},
    viewModel: MateriViewModel = viewModel()
) {
    val materi = viewModel.selectedMateri

    // ===== NULL GUARD =====
    if (materi == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No materi selected")
        }
        return
    }

    // ===== DI SINI materi SUDAH NON-NULL =====
    Scaffold(
        bottomBar = { NavbarBawah(
            onHomeClick = {},
            onProfileClick = onProfileClick
        ) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = materi.name,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                text = materi.targetTime,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = materi.progress / 100f
            )

            Button(
                onClick = onStartTimer
            ) {
                Text("START STUDY")
            }
        }
    }
}


@Composable
fun InfoBox(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}
