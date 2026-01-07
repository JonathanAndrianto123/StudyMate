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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ===== SCREEN =====
@Composable
fun ProfileScreen(
    onEditClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onHomeClicked: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White
            ) {
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
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F4D3A)
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // ===== TOP BAR =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { onBackClicked() }
                )

                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F4D3A)
                )

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Hallo, Mingyu!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F4D3A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== PROFILE ICON =====
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Color(0xFFEAEAEA),
                        shape = CircleShape
                    ),
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

            ProfileTextField("Name", "Mingyu")
            ProfileTextField("Email", "mingyu17@gmail.com")
            ProfileTextField("Password", "************")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onEditClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F4D3A)
                )
            ) {
                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ===== REUSABLE FIELD =====
@Composable
fun ProfileTextField(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F4D3A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ===== PREVIEW =====
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
