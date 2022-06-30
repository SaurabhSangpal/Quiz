package com.sangpal.quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sangpal.quiz.ui.theme.QuizTheme
import kotlinx.coroutines.launch

class GameOverActivity : ComponentActivity() {
    private var score: Int = 0
    private var timeSaved: Int = 0
    private var level: Int = 0
    private var canTryAgain: Boolean = true

    var mIsInterstitialLoading: Boolean = false
    var mInterstitialAd: InterstitialAd? = null

    var mIsRewardedLoading: Boolean = false
    var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        score = intent.getIntExtra("score", 0)
        timeSaved = intent.getIntExtra("timeSaved", 0)
        level = intent.getIntExtra("level", 0)
        canTryAgain = intent.getBooleanExtra("canTryAgain", true)

        mIsInterstitialLoading = true
        loadInterstitialAd()
        mIsRewardedLoading = true
        loadRewardedAd()
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
        val snackState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        SnackbarHost(hostState = snackState)
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
                Text("Score: ")
                Text(score.toString())
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Time Saved: ")
                Text("$timeSaved seconds")
            }
            Spacer(modifier = Modifier.size(50.dp))
            Button(onClick = {
                if (mIsRewardedLoading) {
                    coroutineScope.launch { snackState.showSnackbar("Loading... Please retry in a moment.") }
                } else {
                    retryLevel()
                }
            }, enabled = canTryAgain, modifier = Modifier.width(130.dp).height(40.dp)) {
                Row {
                    Image(painterResource(R.drawable.featured_video), "Share Icon")
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Try Again")
                }
            }
            Spacer(modifier = Modifier.size(25.dp))
            Button(onClick = {
                loadMainMenu()
            }, modifier = Modifier.width(130.dp).height(40.dp)) {
                Text("Main Menu")
            }
        }

        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(onClick = { shareScore() }, modifier = Modifier.width(110.dp).height(40.dp)) {
                Row {
                    Image(painterResource(R.drawable.share), "Share Icon")
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Share")
                }
            }
        }
    }

    private fun loadMainMenu() {
        if (mIsInterstitialLoading)
            return
        if (mInterstitialAd != null)
            mInterstitialAd?.show(this)
        else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun retryLevel() {
        if (mIsRewardedLoading)
            return
        if (mRewardedAd != null) {
            mRewardedAd?.show(this, object : OnUserEarnedRewardListener {
                override fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    val intent = Intent(baseContext, GameActivity::class.java)
                    intent.putExtra("level", level)
                    intent.putExtra("canTryAgain", false)
                    // TODO: Add additional data to allow continuing game.
                    startActivity(intent)
                }
            })
        }
    }

    private fun shareScore() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "I scored $score in quiz. Can you beat my score?")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun loadInterstitialAd() {
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            INTERSTITIAL,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    mIsInterstitialLoading = false
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mIsInterstitialLoading = false
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

    private fun loadRewardedAd() {
        var request = AdRequest.Builder().build()

        RewardedAd.load(this, REWARDED, request, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                mIsRewardedLoading = false
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                super.onAdLoaded(rewardedAd)
                mIsRewardedLoading = false
                rewardedAd.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            mRewardedAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            mRewardedAd = null
                        }
                    }
                mRewardedAd = rewardedAd
            }
        })
    }
}