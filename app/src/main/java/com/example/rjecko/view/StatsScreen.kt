package com.example.rjecko.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rjecko.model.StatsModel
import com.example.rjecko.viewmodel.PlayViewModel

@Composable
fun StatsScreen(navController: NavController, username: String, stats: StatsModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Stats for", fontSize = 50.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = username, fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 80.dp))


        Text(text = "6 guesses: ${stats.guess6}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "5 guesses: ${stats.guess5}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "4 guesses: ${stats.guess4}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "3 guesses: ${stats.guess3}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "2 guesses: ${stats.guess2}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "1 guess: ${stats.guess1}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Didn't guess word: ${stats.noguess}", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {  navController.popBackStack()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text("Back")
        }

    }
}

