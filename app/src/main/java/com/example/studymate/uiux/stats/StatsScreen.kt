package com.example.studymate.uiux.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun StatsScreen(
    viewModel: StatsViewModel
) {
    val stats by viewModel.stats.collectAsState()
    val totalDistractions by viewModel.totalDistractions.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Statistics", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))
            
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFCDD2)
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Total Distractions", fontWeight = FontWeight.Bold)
                    Text("$totalDistractions times phone flipped", fontSize = 20.sp)
                }
            }

            Text("Course Progress", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            stats.forEach {
                Text(it.name)
                LinearProgressIndicator(
                    progress = { it.progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
