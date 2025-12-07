package com.example.oink

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.oink.navigation.AppNavGraph
import com.google.firebase.FirebaseApp
import com.example.oink.utils.LocaleHelper

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        // Apply persisted locale to the base context before creation
        val wrapped = LocaleHelper.applyLocaleToContext(newBase)
        super.attachBaseContext(wrapped)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}
