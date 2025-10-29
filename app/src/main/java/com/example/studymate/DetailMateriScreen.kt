package com.example.studymate

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailMateriScreen() {
    var studyTimeToday by remember { mutableStateOf("0 hr 00 min") }
    var studyTimeTotal by remember { mutableStateOf("0 hr 00 min") }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }
    val targetTime = "02 : 15 : 00"
    val progress = 40 // contoh aja

    Scaffold(
        bottomBar = { NavbarBawah() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Matematika Teknik",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Target Time",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = targetTime,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C4D43)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your Progress",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = Color(0xFF1C4D43),
                    trackColor = Color(0xFFE6E6E6)
                )
                Text(
                    "$progress%",
                    fontSize = 13.sp,
                    color = Color(0xFF1C4D43),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Study Time Today", fontSize = 13.sp, color = Color.Gray)
                    Text(studyTimeToday, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Study Time Total", fontSize = 13.sp, color = Color.Gray)
                    Text(studyTimeTotal, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Set Timer",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeField(
                    value = hour,
                    onValueChange = { hour = it },
                    label = "Hour",
                    placeholder = "00"
                )
                Text(":", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                TimeField(
                    value = minute,
                    onValueChange = { minute = it },
                    label = "Minute",
                    placeholder = "00"
                )
                Text(":", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                TimeField(
                    value = second,
                    onValueChange = { second = it },
                    label = "Second",
                    placeholder = "00"
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            Button(
                onClick = { /* */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C4D43)),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("START", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun TimeField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .width(90.dp)
            .height(70.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DetailMateriScreenPreview() {
    DetailMateriScreen()
}
