package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.MainRoutineApp
import com.example.ui.RoutineViewModel
import com.example.ui.theme.DailyRoutineTheme

class MainActivity : ComponentActivity() {

    private val viewModel: RoutineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyRoutineTheme {
                MainRoutineApp(viewModel = viewModel)
            }
        }
    }
}
