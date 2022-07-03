package `in`.stellargames.quizly

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import `in`.stellargames.quizly.ui.theme.Green
import `in`.stellargames.quizly.ui.theme.QuizTheme
import `in`.stellargames.quizly.ui.theme.Red
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class GameActivity : ComponentActivity() {
    private lateinit var allQuestions: AllQuestions
    private var timer: CountDownTimer? = null

    private var currentLevel = 1
    private var hasBeenPaused = false

    private var timeSaved: Int = 0
    private var remainingTime = 0

    private var canTryAgain: Boolean = true

    private var canPlayAudio: Boolean = true

    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentLevel = intent.getIntExtra("level", 1)
        canTryAgain = intent.getBooleanExtra("canTryAgain", true)

        canPlayAudio = getSharedPreferences("quizprefs", MODE_PRIVATE).getBoolean("audio", true)

        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val file = assets.open("all_questions.json")
                    try {
                        allQuestions = Json.decodeFromStream<Root>(file).allQuestions
                    } catch (e: Exception) {
                        return@Surface
                    }
                    file.close()
                    DisplayQuestion(allQuestions)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AndroidView(factory = { context ->
                            AdView(context).apply {
                                setAdSize(AdSize.BANNER)
                                adUnitId = BANNER
                                loadAd(AdRequest.Builder().build())
                                adListener = object : AdListener() {}
                            }
                        })
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasBeenPaused)
            loadMainMenu()
    }

    override fun onPause() {
        super.onPause()
        hasBeenPaused = true
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
        val (score, onScoreChange) = remember {
            mutableStateOf(
                if (currentLevel > 1) allQuestions.getScoreByLvl(
                    currentLevel - 1
                ) else 0
            )
        }
        DisplayScore(score = score)

        val (time, onTimeChange) = remember { mutableStateOf(60) }
        val (timerStarted, onTimerStarted) = remember { mutableStateOf(false) }

        if (timer == null) {
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = (millisUntilFinished / 1000).toInt()
                    onTimeChange(remainingTime)
                }

                override fun onFinish() {
                    loadGameOverActivity(score)
                }
            }
        }
        if (!timerStarted && submitText == "Submit") {
            timer?.start()
            onTimerStarted(true)
        }

        DisplayTimer(time = time)

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
                        modifier = buildOptionModifier(
                            index = 1,
                            submitText = submitText,
                            selectedOption = selectedOption,
                            answer = question.ans,
                            onOptionSelected = onOptionSelected
                        )
                    ) {
                        RadioButton(
                            selected = (selectedOption == 1),
                            onClick = { if (submitText == "Submit") onOptionSelected(1) })
                        Text(question.a)
                    }
                }
                Column(Modifier.weight(0.5f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = buildOptionModifier(
                            index = 2,
                            submitText = submitText,
                            selectedOption = selectedOption,
                            answer = question.ans,
                            onOptionSelected = onOptionSelected
                        )
                    ) {
                        RadioButton(
                            selected = (selectedOption == 2),
                            onClick = { if (submitText == "Submit") onOptionSelected(2) })
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
                        modifier = buildOptionModifier(
                            index = 3,
                            submitText = submitText,
                            selectedOption = selectedOption,
                            answer = question.ans,
                            onOptionSelected = onOptionSelected
                        )
                    ) {
                        RadioButton(
                            selected = (selectedOption == 3),
                            onClick = { if (submitText == "Submit") onOptionSelected(3) })
                        Text(question.c)
                    }
                }
                Column(Modifier.weight(0.5f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = buildOptionModifier(
                            index = 4,
                            submitText = submitText,
                            selectedOption = selectedOption,
                            answer = question.ans,
                            onOptionSelected = onOptionSelected
                        )
                    ) {
                        RadioButton(
                            selected = (selectedOption == 4),
                            onClick = { if (submitText == "Submit") onOptionSelected(4) })
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
                        currentLevel++
                        if (currentLevel > AllQuestions.TOTAL_LEVELS) {
                            loadGameOverActivity(score)
                            return@Button
                        }
                        onOptionSelected(-1)
                        onSubmitTextChange("Submit")
                        onQuestionChange(allQuestions.getQuestionByLvl(currentLevel))
                        return@Button
                    } else if (submitText == "Back") {
                        loadGameOverActivity(score)
                        return@Button
                    }
                    if (selectedOption == question.ans) {
                        if (canPlayAudio) {
                            val chime = MediaPlayer.create(baseContext, R.raw.correct)
                            chime.start()
                            chime.setOnCompletionListener { chime.release() }
                        }
                        onScoreChange(allQuestions.getScoreByLvl(currentLevel))
                        timeSaved += remainingTime
                        onSubmitTextChange("Next")
                    } else {
                        if (canPlayAudio) {
                            val chime = MediaPlayer.create(baseContext, R.raw.incorrect)
                            chime.start()
                            chime.setOnCompletionListener { chime.release() }
                        }
                        onSubmitTextChange("Back")
                    }
                    timer?.cancel()
                    onTimerStarted(false)
                }, enabled = selectedOption != -1, modifier = Modifier.width(130.dp)) {
                    Text(submitText)
                }
            }
        }
    }

    private fun loadGameOverActivity(score: Int) {
        val intent = Intent(baseContext, GameOverActivity::class.java)
        intent.putExtra("level", currentLevel)
        intent.putExtra("score", score)
        intent.putExtra("timeSaved", timeSaved)
        intent.putExtra("canTryAgain", canTryAgain)
        startActivity(intent)
    }

    @Composable
    private fun DisplayScore(score: Int) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            val st = score.toString()
            Text("Score: $st")
        }
    }

    @Composable
    private fun DisplayTimer(time: Int) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(time.toString())
        }
    }

    @Composable
    private fun buildOptionModifier(
        index: Int,
        submitText: String,
        selectedOption: Int,
        answer: Int,
        onOptionSelected: (Int) -> Unit
    ): Modifier {
        return Modifier
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = (selectedOption == index),
                onClick = { if (submitText == "Submit") onOptionSelected(index) })
            .fillMaxWidth()
            .background(
                if (selectedOption == index && submitText == "Next") Green
                else if (selectedOption == index && submitText == "Back") Red
                else if (selectedOption != index && index == answer && submitText == "Back") Green
                else MaterialTheme.colors.background
            )
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