package com.sangpal.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.MobileAds
import com.sangpal.quiz.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) { }
        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val btnModifier = Modifier
                            .width(200.dp)
                            .height(40.dp)
                        Button(onClick = { startGame() }, modifier = btnModifier) {
                            Text("Start Game")
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = {}, modifier = btnModifier) {
                            Text("Settings")
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = {}, modifier = btnModifier) {
                            Text("How To Play")
                        }
                    }
                }
            }
        }
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}