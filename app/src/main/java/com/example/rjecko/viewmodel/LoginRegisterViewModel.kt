package com.example.rjecko.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rjecko.model.LoginRegisterModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LoginRegisterViewModel(application: Application):AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()



    val registrationStatus = MutableLiveData<Boolean>()
    val loginStatus = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun register(email: String?, username: String?, password: String?) {
        if (email.isNullOrEmpty() || !email.contains("@")) {
            Toast.makeText(getApplication(), "Invalid email address", Toast.LENGTH_SHORT).show()
            registrationStatus.value = false
            return
        }

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(getApplication(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
            registrationStatus.value = false
            return
        }

        try {
            val docRef = db.collection("Users").document(username)

            // Check if the username already exists
            val infoDocumentSnapshot = docRef.collection("Info").document("Info").get().await()

            if (infoDocumentSnapshot.exists() && infoDocumentSnapshot.getString("name") != null) {
                Toast.makeText(getApplication(), "Username already exists", Toast.LENGTH_SHORT).show()
                registrationStatus.value = false
                return
            }



            // Username does not exist, proceed with registration
            val infoData = hashMapOf(
                "email" to email,
                "name" to username,
                "password" to password,
                "date" to "",
                "last-guess" to "0",
                "last-word" to "",
                "finished" to "false"
            )

            // Use a transaction to ensure consistency
            db.runTransaction { transaction ->
                transaction.set(docRef.collection("Info").document("Info"), infoData)
                transaction.set(docRef.collection("Stats").document("Stats"), getDefaultStatsData())
            }.await()

            Toast.makeText(getApplication(), "Registration successful", Toast.LENGTH_SHORT).show()
            registrationStatus.value = true

        } catch (e: Exception) {
            Toast.makeText(getApplication(), "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
            registrationStatus.value = false
        }
    }

    private fun getDefaultStatsData(): HashMap<String, Any> {
        return hashMapOf(
            "guess1" to 0,
            "guess2" to 0,
            "guess3" to 0,
            "guess4" to 0,
            "guess5" to 0,
            "guess6" to 0,
            "noguess" to 0
        )
    }


   suspend fun login(username: String?, password: String?){
       if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
           Toast.makeText(getApplication(), "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
           loginStatus.value = false
           return
       }
       try {
           val docRef = db.collection("Users").document(username)
           val infoDocumentSnapshot = docRef.collection("Info").document("Info").get().await()

           if (!infoDocumentSnapshot.exists()) {
               Toast.makeText(getApplication(), "Username not found", Toast.LENGTH_SHORT).show()
               loginStatus.value = false
               return
           }

           val storedPassword = infoDocumentSnapshot.getString("password")

           if (storedPassword == password) {
               Toast.makeText(getApplication(), "Login successful", Toast.LENGTH_SHORT).show()
               loginStatus.value = true
           } else {
               Toast.makeText(getApplication(), "Password is incorrect", Toast.LENGTH_SHORT).show()
               loginStatus.value = false
           }

       } catch (e: Exception) {
           Toast.makeText(getApplication(), "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
           loginStatus.value = false
       }
    }

}