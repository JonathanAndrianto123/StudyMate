package com.example.studymate.uiux.auth

import androidx.lifecycle.viewmodel.compose.viewModel
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUp(
    viewModel: SignUpViewModel = viewModel(),
    onSignUpClicked: () -> Unit = {},
    onSignInClicked: () -> Unit = {}
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // ===== HEADER =====
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

            // ===== INPUTS =====
            RegisterTextField(
                hint = "Complete Name",
                icon = Icons.Default.Person,
                value = viewModel.name,
                onValueChange = viewModel::onNameChange
            )

            RegisterTextField(
                hint = "Email Address",
                icon = Icons.Default.Email,
                value = viewModel.email,
                onValueChange = viewModel::onEmailChange
            )

            RegisterTextField(
                hint = "Create Password",
                icon = Icons.Default.Lock,
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChange,
                isPassword = true
            )

            RegisterTextField(
                hint = "Verify Password",
                icon = Icons.Default.Lock,
                value = viewModel.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ===== FOOTER =====
            Button(
                onClick = {
                    viewModel.signUp()
                    onSignUpClicked()
                },
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

            Row(
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


@Composable
fun RegisterTextField(
    hint: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint) },
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


@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    SignUp (
        onSignUpClicked = {},
        onSignInClicked = {}
    )
}