package `in`.stellargames.quizly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.MobileAds
import `in`.stellargames.quizly.ui.theme.QuizTheme
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var firebase: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebase = Firebase.analytics
        MobileAds.initialize(this) { }
        val prefs = getSharedPreferences("quizprefs", MODE_PRIVATE)
        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val displayRules =
                        remember { mutableStateOf(intent.getBooleanExtra("displayRules", false)) }
                    val showSettings = remember { mutableStateOf(false) }
                    // Custom back functionality to get out of settings panel.
                    onBackPressedDispatcher.addCallback(object :
                        OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (showSettings.value) {
                                showSettings.value = false
                            } else if (displayRules.value) {
                                displayRules.value = false
                            } else {
                                finish()
                            }
                        }
                    })
                    if (displayRules.value) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Rules", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                            Spacer(modifier = Modifier.size(15.dp))
                            Text("Answer the questions correctly in the shortest amount of time for high score!")
                            Text(
                                """Please note that pausing the game is not allowed.
You are also not allowed to press the back or home button while the game is in progress.
The game will reset if it is sent to background."""
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                        }
                        Column(
                            modifier = Modifier.padding(
                                10.dp
                            ), verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Button(
                                onClick = { displayRules.value = false },
                                modifier = Modifier.width(100.dp)
                            ) {
                                Text("Back")
                            }
                        }
                    } else if (!showSettings.value) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                getString(R.string.app_name),
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp
                            )
                            Spacer(modifier = Modifier.size(300.dp))
                        }
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
                            Button(
                                onClick = { showSettings.value = true },
                                modifier = btnModifier
                            ) {
                                Text("Settings")
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Button(
                                onClick = { displayRules.value = true },
                                modifier = btnModifier
                            ) {
                                Text("Rules")
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(
                                10.dp
                            ), verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Button(
                                onClick = { showSettings.value = false },
                                modifier = Modifier.width(100.dp)
                            ) {
                                Text("Back")
                            }
                        }
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                val audioEnabled =
                                    remember { mutableStateOf(prefs.getBoolean("audio", true)) }
                                Text("Play Audio")
                                Checkbox(
                                    checked = audioEnabled.value,
                                    onCheckedChange = {
                                        val state = prefs.getBoolean("audio", true)
                                        prefs.edit().putBoolean("audio", !state).apply()
                                        audioEnabled.value = !state
                                    })
                            }
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