package com.example.admin.makemoneyonline

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.admin.makemoneyonline.adapter.TabPagerAdapter
import com.example.admin.makemoneyonline.presenter.LoginPresenter
import com.example.admin.makemoneyonline.presenter.MainActivityPresenter
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*

//import AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener


const val GAME_LENGTH_MILLISECONDS = 3000L
const val COUNTER_TIME = 30L
const val GAME_OVER_REWARD = 1
const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

class MainActivity : AppCompatActivity(), RewardedVideoAdListener {

    private lateinit var mRewardedVideoAd: RewardedVideoAd

    private lateinit var mInterstitialAd: InterstitialAd
    private var mCountDownTimer: CountDownTimer? = null
    private var mGameIsInProgress = false
    private var mTimerMilliseconds = 0L


    private var mCoinCount: Int = 0
    private var mGameOver = false
    private var mGamePaused = false
    private var mTimeRemaining: Long = 0L
    private var mCountDownTimerVideo: CountDownTimer? = null

    private val mMainActivityPresenter = MainActivityPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FuelManager.instance.basePath = "http://142.93.29.45:8888";

        val fragmentAdapter = TabPagerAdapter(supportFragmentManager)
        if (viewpager_main != null) {
            viewpager_main.adapter = fragmentAdapter
            tab_layout.setupWithViewPager(viewpager_main)
        }
        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(this, getResources().getString(R.string.app_id))

        // Create an ad request. If you're running this on a physical device, check your logcat to
        // learn how to enable test ads for it. Look for a line like this one:
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
//         Start loading the ad in the background.
        ad_view.loadAd(adRequest)


        // Create the InterstitialAd and set it up.
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = getResources().getString(R.string.add_unit_id_interstitial)
            adListener = (object : AdListener() {
                override fun onAdClosed() {
                    startGame()
                }
            })
        }

        // Create the "retry" button, which triggers an interstitial between game plays.
        retry_button.visibility = View.INVISIBLE
        retry_button.setOnClickListener { showInterstitial() }

        // Kick off the first play of the "game."
        startGame()


        //Video
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this

        loadRewardedVideoAd()


        // Create the "retry" button, which tries to show a rewarded video ad between game plays.
        video_retry_button.visibility = View.INVISIBLE
        video_retry_button.setOnClickListener { startGameVideo() }

        // Create the "show" button, which shows a rewarded video if one is loaded.
        show_video_button.visibility = View.INVISIBLE
        show_video_button.setOnClickListener { showRewardedVideo() }

        // Display current coin count to user.
        coin_count_text.text = "Coins: $mCoinCount"
        startGameVideo()

    }

    // Called when leaving the activity
    public override fun onPause() {
        ad_view.pause()
        pauseGame()
        mCountDownTimer?.cancel()
        mCountDownTimerVideo?.cancel()
        mRewardedVideoAd.pause(this)
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        ad_view.resume()
        if (!mGameOver && mGamePaused) {
            resumeGameVideo()
        }
        mRewardedVideoAd.resume(this)
        if (mGameIsInProgress) {
            resumeGame(mTimerMilliseconds)
        }
    }

    private fun pauseGame() {
        mCountDownTimer?.cancel()
        mCountDownTimerVideo?.cancel()
        mGamePaused = true
    }

    private fun resumeGameVideo() {
        createTimerVideo(mTimeRemaining)
        mGamePaused = false
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        ad_view.destroy()
        super.onDestroy()
        //mRewardedVideoAd.destroy(this)
    }



    // Create the game timer, which counts down to the end of the level
    // and shows the "retry" button.
    private fun createTimer(milliseconds: Long) {
        mCountDownTimer?.cancel()

        mCountDownTimer = object : CountDownTimer(milliseconds, 50) {
            override fun onTick(millisUntilFinished: Long) {
                mTimerMilliseconds = millisUntilFinished
                timer.text = "seconds remaining: ${millisUntilFinished / 1000 + 1}"
            }

            override fun onFinish() {
                mGameIsInProgress = false
                timer.text = "done!"
                retry_button.visibility = View.VISIBLE
            }
        }
    }


    private fun createTimerVideo(time: Long) {
        mCountDownTimerVideo?.cancel()

        mCountDownTimerVideo = object : CountDownTimer(time * 1000, 50) {
            override fun onTick(millisUnitFinished: Long) {
                mTimeRemaining = millisUnitFinished / 1000 + 1
                timer_video.text = "seconds remaining: $mTimeRemaining"
            }

            override fun onFinish() {
                if (mRewardedVideoAd.isLoaded) {
                    show_video_button.visibility = View.VISIBLE
                }
                timer_video.text = "The game has ended!"
                addCoins(GAME_OVER_REWARD)
                video_retry_button.visibility = View.VISIBLE
                mGameOver = true
            }
        }
        mCountDownTimerVideo?.start()
    }

    // Show the ad if it's ready. Otherwise toast and restart the game.
    private fun showInterstitial() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Toast.makeText(this, "Ad wasn't loaded.", Toast.LENGTH_SHORT).show()
            startGame()
        }
    }

    // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
    private fun startGame() {
        if (!mInterstitialAd.isLoading && !mInterstitialAd.isLoaded) {
            // Create an ad request. If you're running this on a physical device, check your logcat
            // to learn how to enable test ads for it. Look for a line like this one:
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            val adRequest = AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build()

            mInterstitialAd.loadAd(adRequest)
        }

        retry_button.visibility = View.INVISIBLE
        resumeGame(GAME_LENGTH_MILLISECONDS)
    }

    private fun resumeGame(milliseconds: Long) {
        // Create a new timer for the correct length and start it.
        mGameIsInProgress = true
        mTimerMilliseconds = milliseconds
        createTimer(milliseconds)
        mCountDownTimer?.start()
    }


    private fun loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded) {
            mRewardedVideoAd.loadAd(AD_UNIT_ID, AdRequest.Builder().build())
        }
    }

    private fun addCoins(coins: Int) {
        mCoinCount += coins
        coin_count_text.text = "Coins: $mCoinCount"
        mMainActivityPresenter.sendRequestGetAllStaff()
    }

    private fun startGameVideo() {
        // Hide the retry button, load the ad, and start the timer.
        video_retry_button.visibility = View.INVISIBLE
        show_video_button.visibility = View.INVISIBLE
        loadRewardedVideoAd()
        createTimerVideo(COUNTER_TIME)
        mGamePaused = false
        mGameOver = false
    }

    private fun showRewardedVideo() {
        show_video_button.visibility = View.INVISIBLE
        if (mRewardedVideoAd.isLoaded) {
            mRewardedVideoAd.show()
        }
    }


    override fun onRewardedVideoAdLeftApplication() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdLoaded() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdOpened() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdClosed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        loadRewardedVideoAd()
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show()
    }

    override fun onRewarded(reward: RewardItem) {
        Toast.makeText(this, "onRewarded! currency: ${reward.type} amount: ${reward.amount}",
                Toast.LENGTH_SHORT).show()
        addCoins(reward.amount)
    }

    override fun onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show()
    }

}
