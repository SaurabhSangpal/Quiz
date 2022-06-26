package com.sangpal.quiz

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sangpal.quiz.ui.theme.QuizTheme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class GameActivity : ComponentActivity() {
    var currentLevel = 1;
    private lateinit var allQuestions: AllQuestions

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val file = assets.open("all_questions.json")
                    try {
                        allQuestions = Json.decodeFromStream<Root>(file).allQuestions
                    } catch (e: Exception) {
                        return@Surface
                    }
                    DisplayQuestion(allQuestions)
                }
            }
        }
    }

    private fun loadMainMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @Composable
    private fun DisplayQuestion(allQuestions: AllQuestions) {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(-1) }
        val (submitText, onSubmitTextChange) = remember { mutableStateOf("Submit") }
        val (question, onQuestionChange) = remember {
            mutableStateOf(
                allQuestions.getQuestionByLvl(
                    currentLevel
                )
            )
        }
        val (score, onScoreChange) = remember { mutableStateOf(0) }
        DisplayScore(score = score, onScoreChange = onScoreChange)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question.
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = question.q)
            }
            // Options.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(Modifier.weight(0.5f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption == 1),
                                onClick = { onOptionSelected(1) })
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (selectedOption == 1),
                            onClick = { onOptionSelected(1) })
                        Text(question.a)
                    }
                }
                Column(Modifier.weight(0.5f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption == 2),
                                onClick = { onOptionSelected(2) })
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (selectedOption == 2),
                            onClick = { onOptionSelected(2) })
                        Text(question.b)
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(Modifier.weight(0.5f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption == 3),
                                onClick = { onOptionSelected(3) })
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (selectedOption == 3),
                            onClick = { onOptionSelected(3) })
                        Text(question.c)
                    }
                }
                Column(Modifier.weight(0.5f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption == 4),
                                onClick = { onOptionSelected(4) })
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (selectedOption == 4),
                            onClick = { onOptionSelected(4) })
                        Text(question.d)
                    }
                }
            }
            // Submit button
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (submitText == "Next") {
                        val chime = MediaPlayer.create(baseContext, R.raw.shooting_star_chime)
                        chime.start()
                        chime.release()
                        onScoreChange(allQuestions.getScoreByLvl(currentLevel))
                        currentLevel++
                        if (currentLevel >= AllQuestions.TOTAL_LEVELS) {
                            val intent = Intent(baseContext, GameOverActivity::class.java)
                            intent.putExtra("score", score)
                            startActivity(intent)
                            return@Button
                        }
                        onOptionSelected(-1)
                        onSubmitTextChange("Submit")
                        onQuestionChange(allQuestions.getQuestionByLvl(currentLevel))
                        return@Button
                    } else if (submitText == "Back") {
                        loadMainMenu()
                        return@Button
                    }
                    if (selectedOption == question.ans) {
                        onSubmitTextChange("Next")
                    } else {
                        onSubmitTextChange("Back")
                    }
                }) {
                    Text(submitText)
                }
            }
        }
    }

    @Composable
    private fun DisplayScore(score: Int, onScoreChange: (Int) -> Unit) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            val st = score.toString()
            Text("$ $st")
        }
    }

    /*
    @Preview(
        showBackground = true,
        name = "Light Mode"
    )
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun PreviewQuestion() {
        QuizTheme {
            DisplayQuestion(
                question = Question(
                    "What is the capital of India?",
                    "Mumbai",
                    "Delhi",
                    "Pune",
                    "Kolkata",
                    2
                ),
            )
        }
    }
     */
}