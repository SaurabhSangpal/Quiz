package `in`.stellargames.quizly

import `in`.stellargames.quizly.ui.theme.QuizTheme
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private enum class UIState {
        MainContent,
        Settings,
        Rules
    }

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
                    val uiState = remember {
                        mutableStateOf(
                            if (intent.getBooleanExtra(
                                    "displayRules",
                                    false
                                )
                            ) UIState.Rules else UIState.MainContent
                        )
                    }

                    // Custom back functionality to get out of settings panel.
                    onBackPressedDispatcher.addCallback(object :
                        OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (uiState.value != UIState.MainContent) {
                                uiState.value = UIState.MainContent
                            } else {
                                finish()
                            }
                        }
                    })
                    DisplayRules(uiState)
                    DisplaySettings(uiState, prefs)
                    ShowMainContent(uiState)
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ShowMainContent(
        uiState: MutableState<UIState>
    ) {
        AnimatedVisibility(
            visible = uiState.value == UIState.MainContent,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(0.dp, 50.dp))
                Text(
                    getString(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .animateEnterExit(
                        enter = expandVertically() + fadeIn(),
                        exit = fadeOut()
                    ),
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
                    onClick = { uiState.value = UIState.Settings },
                    modifier = btnModifier
                ) {
                    Text("Settings")
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = { uiState.value = UIState.Rules },
                    modifier = btnModifier
                ) {
                    Text("Rules")
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun DisplaySettings(
        uiState: MutableState<UIState>,
        prefs: SharedPreferences
    ) {
        AnimatedVisibility(
            visible = uiState.value == UIState.Settings,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(0.dp, 50.dp))
                Text(
                    text = "Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .animateEnterExit(enter = expandVertically() + fadeIn(), exit = fadeOut()),
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

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun DisplayRules(
        uiState: MutableState<UIState>
    ) {
        AnimatedVisibility(
            visible = uiState.value == UIState.Rules,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(0.dp, 50.dp))
                Text(
                    text = "Rules",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .animateEnterExit(enter = expandVertically() + fadeIn(), exit = fadeOut()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text("Answer the questions correctly in the shortest amount of time for high score!")
                Text(
                    """Please note that pausing the game is not allowed.
You are also not allowed to press the back or home button while the game is in progress.
The game will reset if it is sent to background."""
                )
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish()
    }
}