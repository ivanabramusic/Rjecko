package com.example.rjecko

import MainScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rjecko.model.StatsModel
import com.example.rjecko.view.BackgroundImage
import com.example.rjecko.view.EndGameScreen
import com.example.rjecko.view.HowToPlayScreen
import com.example.rjecko.view.LoginRegisterScreen
import com.example.rjecko.view.LoginScreen
import com.example.rjecko.view.MainGameScreen
import com.example.rjecko.view.RegisterScreen
import com.example.rjecko.view.StatsScreen
import com.example.rjecko.viewmodel.LoginRegisterViewModel
import com.example.rjecko.viewmodel.PlayViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = application
        setContent {
           val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "loginRegisterScreen"){
                composable("loginRegisterScreen"){
                    BackgroundImage(modifier = Modifier)
                    LoginRegisterScreen(navController)
                }
                composable("registerScreen"){
                    BackgroundImage(modifier = Modifier)
                    RegisterScreen(navController, LoginRegisterViewModel(application))
                }
                composable("loginScreen"){
                    BackgroundImage(modifier = Modifier)
                    LoginScreen(navController, LoginRegisterViewModel(application))
                }
                composable("mainScreen/{username}", arguments = listOf(navArgument("username"){type = NavType.StringType})){
                        backStackEntry  ->
                    val username = backStackEntry .arguments?.getString("username")?:""
                    BackgroundImage(modifier = Modifier)
                    MainScreen(navController, PlayViewModel(application), username)
                }
                composable("howToPlayScreen"){
                    BackgroundImage(modifier = Modifier)
                    HowToPlayScreen(navController)
                }
                composable(
                    route = "statsScreen/{username}/{guess1}/{guess2}/{guess3}/{guess4}/{guess5}/{guess6}/{noguess}",
                    arguments = listOf(
                        navArgument("username") { type = NavType.StringType },
                        navArgument("guess1") { type = NavType.IntType },
                        navArgument("guess2") { type = NavType.IntType },
                        navArgument("guess3") { type = NavType.IntType },
                        navArgument("guess4") { type = NavType.IntType },
                        navArgument("guess5") { type = NavType.IntType },
                        navArgument("guess6") { type = NavType.IntType },
                        navArgument("noguess") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: return@composable
                    val guess1 = backStackEntry.arguments?.getInt("guess1") ?: 0
                    val guess2 = backStackEntry.arguments?.getInt("guess2") ?: 0
                    val guess3 = backStackEntry.arguments?.getInt("guess3") ?: 0
                    val guess4 = backStackEntry.arguments?.getInt("guess4") ?: 0
                    val guess5 = backStackEntry.arguments?.getInt("guess5") ?: 0
                    val guess6 = backStackEntry.arguments?.getInt("guess6") ?: 0
                    val noguess = backStackEntry.arguments?.getInt("noguess") ?: 0
                    val stats = StatsModel(guess6, guess5, guess4, guess3, guess2, guess1, noguess)
                    BackgroundImage(modifier = Modifier)
                    StatsScreen(navController, username, stats)
                }
                composable("mainGameScreen/{username}/{answer}", arguments = listOf(navArgument("username"){type = NavType.StringType},
                    navArgument("answer"){type = NavType.StringType})){
                        backStackEntry  ->
                    val username = backStackEntry .arguments?.getString("username")?:""
                    val answer = backStackEntry.arguments?.getString("answer")?:""
                    BackgroundImage(modifier = Modifier)
                    MainGameScreen(navController, PlayViewModel(application), username, answer)
                }
                composable("endGameScreen/{username}/{attempts}/{success}/{answer}",
                    arguments = listOf(
                        navArgument("username") { type = NavType.StringType },
                        navArgument("attempts") { type = NavType.StringType },
                        navArgument("success") { type = NavType.BoolType },
                        navArgument("answer") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val attempts = backStackEntry.arguments?.getString("attempts") ?: ""
                    val success = backStackEntry.arguments?.getBoolean("success") ?: false
                    val answer = backStackEntry.arguments?.getString("answer") ?: ""
                    BackgroundImage(modifier = Modifier)
                    EndGameScreen(navController = navController, viewModel = PlayViewModel(application), username = username, attempts = attempts, success = success, answer = answer)
                }
            }

        }
    }
}

