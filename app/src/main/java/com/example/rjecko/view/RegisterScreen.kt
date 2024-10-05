package com.example.rjecko.view

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun RegisterScreen(navController: NavController, viewModel: LoginRegisterViewModel)
{
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val registrationStatus by viewModel.registrationStatus.observeAsState()

    val scope = rememberCoroutineScope()


    LaunchedEffect(registrationStatus) {
        registrationStatus?.let {
            if (it) {
                navController.navigate("mainScreen/$username") {
                    popUpTo("registerScreen") { inclusive = true }
                }
                viewModel.registrationStatus.value = null
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center

    ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REGISTER",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 100.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true,
            maxLines = 1,
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
        ){
        Button(
            onClick = {  scope.launch {
                viewModel.register(email, username, password)
                }
                },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
        ) {
            Text("REGISTER")
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