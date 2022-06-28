package com.sangpal.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sangpal.quiz.ui.theme.QuizTheme

class GameOverActivity : ComponentActivity() {
    private var score: Int = 0
    private var timeSaved: Int = 0
    var mAdIsLoading: Boolean = false
    var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        score = intent.getIntExtra("score", 0)
        timeSaved = intent.getIntExtra("timeSaved", 0)
        mAdIsLoading = true
        loadInterstitialAd()
        setContent {
            QuizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DisplayGameOver()
                }
            }
        }
    }

    @Composable
    private fun DisplayGameOver() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val gameOverText = if (score == 1000000) "Game Complete!" else "Game Over"
            Text(
                gameOverText, fontWeight = FontWeight.Bold, fontSize = 30.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Score: ")
                Text(score.toString())
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Time Saved: ")
                Text("$timeSaved seconds")
            }
            Spacer(modifier = Modifier.size(50.dp))
            Button(onClick = { loadMainMenu() }) {
                Text("Main Menu")
            }
        }

        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text("Share")
            }
        }
    }

    private fun loadMainMenu() {
        if (mAdIsLoading)
            return
        if (mInterstitialAd != null)
            mInterstitialAd?.show(this)
        else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadInterstitialAd() {
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    mAdIsLoading = false
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mAdIsLoading = false
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                mInterstitialAd = null
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                mInterstitialAd = null
                                loadMainMenu()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                // Called when ad fails to show.
                                mInterstitialAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                mInterstitialAd = null
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                mInterstitialAd = null
                            }
                        }
                    mInterstitialAd = interstitialAd
                }
            })
    }
}