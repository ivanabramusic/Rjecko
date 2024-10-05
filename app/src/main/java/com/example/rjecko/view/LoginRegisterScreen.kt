package com.example.rjecko.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun LoginRegisterScreen(navController: NavController)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RJEČKO",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 300.dp)

        )
        Button(
            onClick = {
                      navController.navigate("loginScreen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
        ) {
            Text(text = "LOGIN")
        }
        Button(
            onClick = {
                      navController.navigate("registerScreen")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "REGISTER")
        }
    }
}
