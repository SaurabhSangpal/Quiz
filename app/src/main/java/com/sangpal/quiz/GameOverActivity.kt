package com.sangpal.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sangpal.quiz.ui.theme.QuizTheme

class GameOverActivity : ComponentActivity() {
    private var score: Int = 0
    private var timeSaved: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        score = intent.getIntExtra("score", 0)
        timeSaved = intent.getIntExtra("timeSaved", 0)
        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }

    @Composable
    private fun DisplayGameOver() {
    }
}