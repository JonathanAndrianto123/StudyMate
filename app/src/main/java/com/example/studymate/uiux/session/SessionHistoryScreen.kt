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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.repository.StudySessionRepository
import com.example.studymate.uiux.session.SessionViewModelFactory

@Composable
fun SessionHistoryScreen(
    // We need to inject ViewModel properly here because it needs dependencies!
    // But this screen might form part of a flow where ViewModel is passed, 
    // or we create it here using the factory.
    // The previous implementation used default viewModel() which CRASHES if factory is needed.
) {
    val context = LocalContext.current
    val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
    val userId = userPrefs.getCurrentUserId()
    
    val db = remember { StudymateDatabase.getDatabase(context) }
    val repository = remember { StudySessionRepository(db.studySessionDao()) }
    val factory = remember { SessionViewModelFactory(context, repository, userId) }
    
    val viewModel: SessionViewModel = viewModel(factory = factory)

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text("Study History", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
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
