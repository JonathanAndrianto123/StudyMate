package com.example.studymate.uiux.materi

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.local.entity.MateriEntity
import com.example.studymate.data.repository.MateriRepository
import androidx.compose.ui.platform.LocalContext
import com.example.studymate.uiux.materi.MateriViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember


@Composable
fun ListMateriScreen(
    viewModel: MateriViewModel,
    onAddClick: () -> Unit,
    onDetailClick: (Int) -> Unit,
    onProfileClick: () -> Unit = {}
) {
    // ===== DATA =====
    val materiList by viewModel.materiList.collectAsState()

    Scaffold(
        bottomBar = {
            NavbarBawah(
                onHomeClick = {},
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
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

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(materiList) { materi ->
                    MateriCard(
                        materi = materi,
                        onDetailClick = {
                            onDetailClick(materi.id)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun MateriCard(
    materi: MateriEntity,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(materi.name, fontWeight = FontWeight.Bold)

                Button(onClick = onDetailClick) {
                    Text("Detail")
                }
            }

            Text("Target Time: ${materi.targetTime}")

            LinearProgressIndicator(
                progress = materi.progress / 100f,
                modifier = Modifier.height(6.dp)
            )
        }
    }
}



@Composable
fun NavbarBawah(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    isHome: Boolean = true
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = isHome,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = !isHome,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ListMateriScreenPreview() {
    ListMateriScreen(
        onAddClick = {},
        onDetailClick = {}
    )
}
*/

