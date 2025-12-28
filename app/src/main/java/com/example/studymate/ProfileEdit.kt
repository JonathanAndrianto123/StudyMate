package com.example.studymate

// ===== IMPORT =====
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ================= SCREEN =================
@Composable
fun EditProfileScreen(
    onBackClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onDiscardClicked: () -> Unit = {},
    onHomeClicked: () -> Unit = {}
) {
    var name by remember { mutableStateOf("Mingyu") }
    var email by remember { mutableStateOf("mingyu17@gmail.com") }
    var password by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ===== TOP BAR =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { onBackClicked() }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Edit Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F4D3A)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ===== PROFILE ICON =====
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFEAEAEA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ===== FORM =====
            ProfileEditTextField(
                label = "Name",
                value = name,
                onValueChange = { name = it }
            )

            ProfileEditTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it }
            )

            ProfileEditTextField(
                label = "Password",
                value = password,
                isPassword = true,
                onValueChange = { password = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ===== SAVE BUTTON =====
            Button(
                onClick = onSaveClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F4D3A)
                )
            ) {
                Text("Save", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== DISCARD BUTTON =====
            Button(
                onClick = onDiscardClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Discard", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))

            // ===== HOME BUTTON =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHomeClicked() }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color(0xFF1F4D3A)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Home",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1F4D3A)
                )
            }
        }
    }
}

// ================= TEXT FIELD =================
@Composable
fun ProfileEditTextField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F4D3A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation =
                if (isPassword)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ================= PREVIEW =================
@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen()
}
