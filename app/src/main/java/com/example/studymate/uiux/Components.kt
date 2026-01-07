package com.example.studymate.uiux

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

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
            focusedContainerColor = Color(0xFFF2F2F2),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    )
}
