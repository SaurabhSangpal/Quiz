package com.sangpal.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sangpal.quiz.ui.theme.QuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DisplayQuestion(
                        question = Question(
                            "What is the capital of India?",
                            "Mumbai",
                            "Pune",
                            "Delhi",
                            "Kanpur",
                            3
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Hello $name!")
        Text(text = "Hello $name!")
        Text(text = "Hello $name!")
    }
}

@Composable
fun DisplayAppTitle() {
    TopAppBar {
        Text(text = "Quiz")
    }
}

@Composable
fun DisplayQuestion(question: Question) {
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
            Text(text = question.question)
        }
        // Options.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RadioButton(selected = false, onClick = { selectAnswer(1) })
            Text(question.optionA)
            RadioButton(selected = false, onClick = { selectAnswer(2) })
            Text(question.optionB)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RadioButton(selected = false, onClick = { selectAnswer(3) })
            Text(question.optionC)
            RadioButton(selected = false, onClick = { selectAnswer(4) })
            Text(question.optionD)
        }
        // Submit button
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun TableScreen() {
    val column1Weight = 0.3f
    val column2Weight = 0.7f;

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Column 1", weight = column1Weight)
                TableCell(text = "Column 2", weight = column2Weight)
            }
        }
    }
}

var selectedAnswer: Int = -1;
fun selectAnswer(selected: Int) {
    selectedAnswer = selected
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuizTheme {
        Greeting("Android")
    }
}