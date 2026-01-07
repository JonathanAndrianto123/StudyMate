package com.example.studymate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studymate.uiux.RegisterTextField

@Composable
fun SignUp(
    onSignUpClicked: () -> Unit = {},
    onSignInClicked: () -> Unit = {}
) {
    // Local state for UI demo
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier.height(60.dp))

            // ===== TITLE =====
            Text(
                text = "Register Now",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F4D3A)
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Create account to start your study tracker with Study Mate!",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))
            RegisterTextField(
                hint = "Complete Name",
                icon = Icons.Default.Person,
                value = name,
                onValueChange = { name = it }
            )

            RegisterTextField(
                hint = "Email Address",
                icon = Icons.Default.Email,
                value = email,
                onValueChange = { email = it }
            )

            RegisterTextField(
                hint = "Create Password",
                icon = Icons.Default.Lock,
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )

            RegisterTextField(
                hint = "Verify Password",
                icon = Icons.Default.Lock,
                value = verifyPassword,
                onValueChange = { verifyPassword = it },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))
            Button (
                onClick = onSignUpClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F4D3A)
                )
            ) {
                Text(
                    text = "SIGN UP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Sign in",
                    fontSize = 13.sp,
                    color = Color(0xFF1F4D3A),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignInClicked() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUp (
        onSignUpClicked = {},
        onSignInClicked = {}
    )
}