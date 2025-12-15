package com.example.studymate.uiux.materi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.repository.MateriRepository
import com.example.studymate.uiux.main.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMateriScreen(
    materiId: Int,
    viewModel: MateriViewModel,
    onBack: () -> Unit,
    onStartTimer: () -> Unit
) {
    val materiState by viewModel.getMateriById(materiId)
        .collectAsState(initial = null)

    val materi = materiState ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Materi") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(materi.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Text("Target Time", color = Color.Gray, fontSize = 13.sp)
            Text(materi.targetTime, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Text("Progress", color = Color.Gray, fontSize = 13.sp)
            LinearProgressIndicator(
                progress = materi.progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Description", color = Color.Gray, fontSize = 13.sp)
            Text(materi.description.ifBlank { "No description" })

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onStartTimer,
                modifier = Modifier.fillMaxWidth()
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
