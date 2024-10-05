package com.example.rjecko.view

import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.rjecko.viewmodel.LoginRegisterViewModel
import com.example.rjecko.viewmodel.PlayViewModel
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
import com.example.rjecko.model.LoginRegisterModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EndGameScreen(navController: NavController, viewModel: PlayViewModel, username: String, attempts: String, success: Boolean, answer: String) {
    val todaysWord = answer
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val onBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate("mainScreen/$username")
            }
        }
    }

    DisposableEffect(key1 = onBackPressedDispatcher) {
        if (onBackPressedDispatcher != null) {
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
        }
        onDispose {
            onBackPressedCallback.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Today's word is:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            todaysWord.forEach { letter ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.LightGray)
                ) {
                    Text(
                        text = letter.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (success) {
            Text(
                text = "You guessed today's word in $attempts attempts!",
                fontSize = 18.sp
            )
        } else {
            Text(
                text = "You didn't guess today's word.",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("mainScreen/$username") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp)
        ) {
            Text("Back to Main Screen")
        }
    }
}