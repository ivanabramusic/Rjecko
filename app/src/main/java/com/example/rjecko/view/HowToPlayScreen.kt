package com.example.rjecko.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun HowToPlayScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Adjusted vertical arrangement
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f) // Occupy maximum space
        ) {
            Text(
                text = "HOW TO PLAY",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Text(
                text = "Guess the word in 6 tries.",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Each guess must be a 5-letter word.",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "COLOR OF LETTER IN WORD:",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GRAY: ",
                    fontSize = 24.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Letter is not in word.",
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "YELLOW: ",
                    fontSize = 24.sp,
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Letter is in word, but not in correct spot.",
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GREEN: ",
                    fontSize = 24.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Letter is in word and in correct spot.",
                    fontSize = 24.sp
                )
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Text("BACK")
        }
    }
}
