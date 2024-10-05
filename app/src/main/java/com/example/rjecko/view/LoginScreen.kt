package com.example.rjecko.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rjecko.model.LoginRegisterModel
import com.example.rjecko.viewmodel.LoginRegisterViewModel
import kotlinx.coroutines.launch

@Composable

fun LoginScreen(navController: NavController, viewModel: LoginRegisterViewModel)
{
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginStatus by viewModel.loginStatus.observeAsState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(loginStatus) {
        loginStatus?.let {
            if (it) {
                navController.navigate("mainScreen/$username") {
                    popUpTo("loginScreen") { inclusive = true }
                }
                viewModel.loginStatus.value = null
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    )
    {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LOGIN",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 100.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        )
    }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.login(username, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
            ) {
                Text("LOGIN")
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("BACK")
            }
        }
    }

}