package com.example.studymate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ====================== SIGN IN SCREEN ======================
@Composable
fun SignIn(
    onSignInClicked: () -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Back Button
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClicked() },
                tint = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Sign in",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F4D3A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Welcome back to Study Mate! Let’s make more journey here.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email
            SignInTextField(
                hint = "Email Address",
                icon = Icons.Filled.Email
            )

            // Password
            SignInTextField(
                hint = "Password",
                icon = Icons.Filled.Lock,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Button
            Button(
                onClick = onSignInClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F4D3A)
                )
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password
            Text(
                text = "Forgot Password?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F4D3A),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onForgotPasswordClicked() }
            )
        }
    }
}

// ====================== TEXT FIELD ======================
@Composable
fun SignInTextField(
    hint: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var text by remember { mutableStateOf("") }

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text(text = hint) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        singleLine = true,
        visualTransformation = if (isPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedContainerColor = Color(0xFFF2F2F2),
            focusedContainerColor = Color(0xFFF2F2F2)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    )
}

// ====================== PREVIEW ======================
@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    MaterialTheme {
        SignIn()
    }
}
