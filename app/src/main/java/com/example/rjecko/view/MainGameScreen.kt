package com.example.rjecko.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rjecko.viewmodel.PlayViewModel



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainGameScreen(navController: NavController, viewModel: PlayViewModel, username: String, answer: String) {
    var currentGuess by remember { mutableStateOf("") }
    val guesses by viewModel.guesses.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display guesses
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            guesses.forEachIndexed { index, guess ->
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    guess.word.forEachIndexed { charIndex, char ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.dp, Color.Black)
                                .background(guess.colors[charIndex])
                        ) {
                            Text(
                                text = char.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        // Input and button at the bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = currentGuess,
                onValueChange = { currentGuess = it.uppercase() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter your guess") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (currentGuess.length == 5) {
                        viewModel.submitGuess(username, currentGuess, navController, answer) // Replace with actual username
                        currentGuess = ""
                    } else {
                        Toast.makeText(context, "Enter a 5 letter word!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Guess")
            }
        }
    }
}



