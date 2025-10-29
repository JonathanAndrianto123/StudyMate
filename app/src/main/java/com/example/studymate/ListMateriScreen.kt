package com.example.studymate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Materi(
    val name: String,
    val targetTime: String,
    val progressTime: String,
    val progress: Int
)

@Composable
fun ListMateriScreen() {
    val materiList = listOf(
        Materi("Matematika Teknik", "2 hour 15 minute", "0%", 0),
        Materi("Algoritma Graph", "3 hour 45 minute", "50%", 50)
    )

    Scaffold(
        bottomBar = { NavbarBawah() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = Color(0xFF1C4D43)
            ) {
                Text("+", color = Color.White, fontSize = 28.sp)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Home",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Welcome to Study Mate!")
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(materiList) { materi ->
                    MateriCard(materi)
                }
            }
        }
    }
}

@Composable
fun MateriCard(materi : Materi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(materi.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C4D43)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Detail", color = Color.White, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Target Time", fontSize = 12.sp, color = Color.Gray)
            Text(materi.targetTime, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text("Progress Time", fontSize = 12.sp, color = Color.Gray)
            Text(materi.progressTime, fontSize = 13.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = materi.progress / 100f,
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp),
                    color = Color(0xFF1C4D43),
                    trackColor = Color(0xFFE6E6E6)
                )
                Text("${materi.progress}%", fontSize = 12.sp, color = Color(0xFF1C4D43))
            }
        }
    }
}


@Composable
fun NavbarBawah() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListMateriScreenPreview() {
    ListMateriScreen()
}
