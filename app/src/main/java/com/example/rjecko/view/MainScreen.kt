import android.os.Build
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.rjecko.model.LoginRegisterModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun MainScreen(navController: NavController, viewModel: PlayViewModel, username: String)
{
    val context = LocalContext.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var backPressedCount by remember { mutableStateOf(0) }

    DisposableEffect(key1 = dispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedCount == 0) {
                    backPressedCount++
                    Toast.makeText(context, "Press back again to exit.", Toast.LENGTH_SHORT).show()

                    // Reset count after 2 seconds
                    viewModel.viewModelScope.launch {
                        delay(2000L)
                        backPressedCount = 0
                    }
                } else {
                    // Exit the application
                    (context as? OnBackPressedDispatcherOwner)?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        }

        dispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "RJEČKO",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp)
                .padding(bottom = 300.dp)
        )

        Text(
            text = "Username: $username",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.TopStart).padding(top = 120.dp, end = 16.dp)
        )

        Column(
            modifier = Modifier.padding(top = 200.dp)
                .align(Alignment.Center),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { viewModel.play(username, navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text("Play")
            }

            Button(
                onClick = {
                    viewModel.showStats(username) { stats ->
                        navController.navigate("statsScreen/$username/${stats.guess1}/${stats.guess2}/${stats.guess3}/${stats.guess4}/${stats.guess5}/${stats.guess6}/${stats.noguess}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text("Stats")
            }

            Button(
                onClick = { navController.navigate("howToPlayScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text("How to Play")
            }

            Button(
                onClick = {  navController.navigate("loginRegisterScreen")
                {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}