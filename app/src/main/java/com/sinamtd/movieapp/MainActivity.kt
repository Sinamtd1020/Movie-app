package com.sinamtd.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sinamtd.movieapp.presentation.home.HomeScreen
import com.sinamtd.movieapp.presentation.navigation.SetUpNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SetUpNavigation()


        }
    }
}
