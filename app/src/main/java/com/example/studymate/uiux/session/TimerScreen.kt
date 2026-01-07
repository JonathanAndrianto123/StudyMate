package com.example.studymate.uiux.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    materiId: Int,
    materiName: String,
    targetTime: String,
    viewModel: SessionViewModel,
    onBack: () -> Unit
) {
    // Set material when screen loads
    androidx.compose.runtime.LaunchedEffect(materiId, materiName, targetTime) {
        viewModel.setMateri(materiId, materiName, targetTime)
    }

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted
        } else {
            // Permission denied
        }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Timer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Display material name
            Text(
                text = materiName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F4D3A)
            )

            Spacer(Modifier.height(16.dp))

            Text(viewModel.formattedTime, fontSize = 60.sp, fontWeight = FontWeight.Bold)

            if (viewModel.targetReached) {
                Text(
                    "TARGET REACHED!",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            } else if (viewModel.isGracePeriod) {
                Text(
                    "Get Ready... Put phone face down",
                    color = Color(0xFF1F4D3A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            } else if (!viewModel.isRunning && viewModel.distractionCount > 0 && viewModel.elapsedSeconds > 0) {
                 Text(
                    "DISTRACTION DETECTED!\nPlease flip phone face down.",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                Text(
                    "Remaining: ${viewModel.formattedRemainingTime}",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { viewModel.startTimer() }) { Text("START") }
                Button(onClick = { viewModel.pauseTimer() }) { Text("PAUSE") }
                Button(onClick = {
                    viewModel.stopTimer {
                        onBack()
                    }
                }) { Text("STOP") }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun TimerScreenPreview() {
//    TimerScreenPreview()
//}