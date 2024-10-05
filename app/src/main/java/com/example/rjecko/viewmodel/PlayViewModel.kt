package com.example.rjecko.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.rjecko.model.Guess

import com.example.rjecko.model.StatsModel
import com.google.firebase.firestore.FieldValue

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class PlayViewModel(application: Application) : AndroidViewModel(application) {


    private val db = FirebaseFirestore.getInstance()
    private val _stats = MutableStateFlow<StatsModel?>(null)
    var todayWord: String = ""



    @RequiresApi(Build.VERSION_CODES.O)


    fun updateWordAndDate(username: String?, word: String, date: String){

        if (username.isNullOrEmpty()) {
            Toast.makeText(getApplication(), "Invalid username", Toast.LENGTH_SHORT).show()
            return
        }

        val userRef = db.collection("Users").document(username).collection("Info").document("Info")

        val updates = hashMapOf<String, Any>(
            "last-word" to word,
            "date" to date,
            "finished" to "false"

        )

        userRef.update(updates)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }

    }
    fun updateAttAndPlayed(username: String?, attempts: Int){
        if (username.isNullOrEmpty()) {
            Toast.makeText(getApplication(), "Invalid username", Toast.LENGTH_SHORT).show()
            return
        }

        var userRef = db.collection("Users").document(username).collection("Info").document("Info")

        val updates = hashMapOf<String, Any>(
            "last-guess" to attempts.toString(),
            "finished" to "true"

        )

        userRef.update(updates)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }

        updates.clear()

        userRef = db.collection("Users").document(username).collection("Stats").document("Stats")

        when (attempts) {
            0 -> updates["noguess"] = FieldValue.increment(1)
            1 -> updates["guess1"] = FieldValue.increment(1)
            2 -> updates["guess2"] = FieldValue.increment(1)
            3 -> updates["guess3"] = FieldValue.increment(1)
            4 -> updates["guess4"] = FieldValue.increment(1)
            5 -> updates["guess5"] = FieldValue.increment(1)
            6 -> updates["guess6"] = FieldValue.increment(1)
        }
        userRef.update(updates)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }
    }


    fun showStats(username: String?, onStatsLoaded: (StatsModel) -> Unit) {
        if (username.isNullOrEmpty()) {
            Toast.makeText(getApplication(), "Invalid username", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val statsRef = db.collection("Users").document(username).collection("Stats").document("Stats")

            statsRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val stats = document.toObject(StatsModel::class.java)

                    if (stats != null) {
                        _stats.value = stats


                        onStatsLoaded(stats)

                    } else {
                        Toast.makeText(getApplication(), "Failed to parse stats", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(getApplication(), "Stats not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(getApplication(), "Error fetching stats: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun play(username: String, navController: NavController){
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date =  currentDate.format(formatter).toString()
        val userRef = db.collection("Users").document(username).collection("Info").document("Info")


        viewModelScope.launch (Dispatchers.IO){
            userRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val lastPlayedDate = document.getString("date")


                    if (lastPlayedDate == date) {
                        // User already played today, navigate to EndGameScreen
                        val isFinished = document.getString("finished")
                        if(isFinished == "false")
                        {
                            val lastWord = document.getString("last-word")
                            if (lastWord != null) {
                                todayWord = lastWord

                            }
                            navController.navigate("mainGameScreen/$username/$todayWord")
                        }
                        else{
                            val lastWord = document.getString("last-word")
                            val lastGuess = document.getString("last-guess")
                            if (lastWord != null) {
                                todayWord = lastWord

                            }
                            if(lastGuess == "0") {
                                navController.navigate("endGameScreen/$username/$lastGuess/false/$lastWord")
                            }
                            else {
                                navController.navigate("endGameScreen/$username/$lastGuess/true/$lastWord")
                            }
                        }

                    }
                    else {
                        // User has not played today, update the date and navigate to MainGameScreen
                        // API CALL
                        viewModelScope.launch {
                            val word = fetchRandomWord()?.uppercase()
                            if (word != null) {
                                todayWord = word

                                updateWordAndDate(username, todayWord, date)
                                navController.navigate("mainGameScreen/$username/$todayWord")
                            } else {
                                // Handle the error
                            }
                        }

                    }
                }
                else {

                }
            }.addOnFailureListener {
                Toast.makeText(getApplication(), "Failed to play game", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private val _guesses = MutableStateFlow<List<Guess>>(emptyList())
    val guesses: StateFlow<List<Guess>> = _guesses

    var attempts = 0
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun submitGuess(username: String, guess: String, navController: NavController, answer: String) {

        if (guess.isEmpty() || guess.length != 5) {
            return
        }

        val answerCharCount = mutableMapOf<Char, Int>()
        val guessStatus = MutableList(5) { 0 } // 0 = unchecked, 1 = correct, 2 = present, 3 = absent
        val colors = MutableList(5) { Color.Gray }

        attempts++
        if (guess == answer || attempts >= 6) {
            val success = guess == answer
            if(success)
            {
                updateAttAndPlayed(username, attempts)

            }
            else{
                updateAttAndPlayed(username, 0)
            }
            navController.navigate("endGameScreen/$username/$attempts/$success/$answer")

        }
        // Count characters in the answer
        for (char in answer) {
            answerCharCount[char] = answerCharCount.getOrDefault(char, 0) + 1
        }

        // First pass: Check for correct positions (Green)
        for (i in 0 until 5) {
            if (guess[i] == answer[i]) {
                colors[i] = Color.Green
                guessStatus[i] = 1
                answerCharCount[guess[i]] = answerCharCount[guess[i]]!! - 1
            }
        }

        // Second pass: Check for present characters (Yellow) and absent (Gray)
        for (i in 0 until 5) {
            if (guessStatus[i] == 0) {
                if (answerCharCount.getOrDefault(guess[i], 0) > 0) {
                    colors[i] = Color.Yellow
                    guessStatus[i] = 2
                    answerCharCount[guess[i]] = answerCharCount[guess[i]]!! - 1
                } else {
                    colors[i] = Color.Gray
                    guessStatus[i] = 3
                }
            }
        }

        val newGuess = Guess(word = guess, colors = colors)
        _guesses.value = _guesses.value + newGuess


    }

    suspend fun fetchRandomWord(): String? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val mediaType = "application/json".toMediaTypeOrNull()
            val body = "{\"timezone\":\"UTC + 2\"}".toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://wordle-game-api1.p.rapidapi.com/word")
                .post(body)
                .addHeader("x-rapidapi-key", "1976c47c4emshcf3952a6995f20fp1b6f96jsn72df3eb3cccd")
                .addHeader("x-rapidapi-host", "wordle-game-api1.p.rapidapi.com")
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (response.isSuccessful && !responseData.isNullOrEmpty()) {
                val jsonObject = JSONObject(responseData)
                return@withContext jsonObject.getString("word").toString() // Replace "key1" with the actual key from your response
            } else {
                return@withContext null
            }
        }
    }
}