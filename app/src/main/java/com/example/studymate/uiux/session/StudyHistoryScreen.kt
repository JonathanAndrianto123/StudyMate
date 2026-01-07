package com.example.studymate.uiux.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.repository.StudySessionRepository
import com.example.studymate.uiux.session.SessionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyHistoryScreen(
    materiId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
    val userId = userPrefs.getCurrentUserId()
    
    val db = remember { StudymateDatabase.getDatabase(context) }
    val repository = remember { StudySessionRepository(db.studySessionDao()) }
    val factory = remember { SessionViewModelFactory(context, repository, userId) }
    
    // We get the viewmodel. Note: If we want to share the same instance as others, 
    // it depends on how this composable is scoped. Here we get a new/existing one.
    val viewModel: SessionViewModel = viewModel(factory = factory)

    LaunchedEffect(materiId) {
        viewModel.loadSessionsForMateri(materiId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Materi History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (viewModel.sessionList.isEmpty()) {
                Text(
                    "No study history yet for this materi.", 
                    color = Color.Gray, 
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            LazyColumn {
                items(viewModel.sessionList) { session ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(session.materiName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Duration: ${session.duration}")
                            Text(session.date)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Distractions: ${session.distractions}", color = Color.Red)
                                Text("Loc: ${session.location}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
