package com.example.studymate.uiux.materi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddMateriScreen(
    onBack: () -> Unit,
    viewModel: MateriViewModel,
    onProfileClick: () -> Unit = {}
) {
    var materiName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }

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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Course",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Add your Course!", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Course Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = materiName,
                onValueChange = { materiName = it },
                placeholder = { Text("Enter Course Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Add a short description or notes here!") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Study Time Goal",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeField(
                    value = hour,
                    onValueChange = { hour = it },
                    placeholder = "00"
                )
                Text(":", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                TimeField(
                    value = minute,
                    onValueChange = { minute = it },
                    placeholder = "00"
                )
                Text(":", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                TimeField(
                    value = second,
                    onValueChange = { second = it },
                    placeholder = "00"
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        viewModel.addMateri(
                            name = materiName,
                            targetTime = normalizeTime(hour, minute, second),
                            description = description
                        )
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C4D43)),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Add", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun TimeField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 2) onValueChange(it.filter { c -> c.isDigit() })
        },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .width(90.dp)
            .height(70.dp)
    )
}

fun normalizeTime(h: String, m: String, s: String): String {
    val hh = h.ifBlank { "00" }.padStart(2, '0')
    val mm = m.ifBlank { "00" }.padStart(2, '0')
    val ss = s.ifBlank { "00" }.padStart(2, '0')
    return "$hh:$mm:$ss"
}


//@Preview(showBackground = true)
//@Composable
//fun AddMateriScreenPreview() {
//    AddMateriScreen()
//}
