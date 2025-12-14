package com.example.studymate.uiux.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onEdit: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            Text("Hello, ${viewModel.name}!")

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = viewModel.name,
                onValueChange = {},
                label = { Text("Name") },
                enabled = false
            )

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {},
                label = { Text("Email") },
                enabled = false
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = onEdit) {
                Text("Edit")
            }
        }
    }
}
