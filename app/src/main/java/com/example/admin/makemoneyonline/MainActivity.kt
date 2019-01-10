package com.example.admin.makemoneyonline

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.admin.makemoneyonline.adapter.TabPagerAdapter
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*


const val GAME_LENGTH_MILLISECONDS = 3000L

class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    private var mCountDownTimer: CountDownTimer? = null
    private var mGameIsInProgress = false
    private var mTimerMilliseconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = TabPagerAdapter(supportFragmentManager)
        if(viewpager_main!=null) {
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

    }

    // Called when leaving the activity
    public override fun onPause() {
        ad_view.pause()
        mCountDownTimer?.cancel()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        ad_view.resume()
        if (mGameIsInProgress) {
            resumeGame(mTimerMilliseconds)
        }
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        ad_view.destroy()
        super.onDestroy()
    }



    // Create the game timer, which counts down to the end of the level
    // and shows the "retry" button.
    private fun createTimer(milliseconds: Long) {
        mCountDownTimer?.cancel()

        mCountDownTimer = object : CountDownTimer(milliseconds, 50) {
            override fun onTick(millisUntilFinished: Long) {
                mTimerMilliseconds = millisUntilFinished
                timer.text = "seconds remaining: ${ millisUntilFinished / 1000 + 1 }"
            }

            override fun onFinish() {
                mGameIsInProgress = false
                timer.text = "done!"
                retry_button.visibility = View.VISIBLE
            }
        }
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


}
