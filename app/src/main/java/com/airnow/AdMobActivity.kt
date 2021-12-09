package com.airnow

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.airnow.databinding.ActivityAdmobBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = AdMobActivity::class.java.simpleName

    private lateinit var mBinding: ActivityAdmobBinding

    private var mBanner: AdView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AdMob Ads SDK.
        MobileAds.initialize(this) {
            Log.e(TAG, it.adapterStatusMap.toString())
        }

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )

        // Set Airnow SDK test mode
        AirnowSdk.setTestMode(true)

        mBinding = ActivityAdmobBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)

        mBinding.loadBannerButton.setOnClickListener(this)
        mBinding.loadInterstitialButton.setOnClickListener(this)
        mBinding.showInterstitialButton.setOnClickListener(this)
        mBinding.loadRewardedButton.setOnClickListener(this)
        mBinding.showRewardedButton.setOnClickListener(this)


        mBinding.toolbar.toolbarUpButton.setOnClickListener(this)
        mBinding.toolbar.toolbarTitle.text = getString(R.string.title_admob_mediation)
        mBinding.sdkVersion.text = getString(R.string.sdk_version, AirnowSdk.getSDKVersion())
        mBinding.appVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }

    override fun onDestroy() {
        destroyAdView()
        super.onDestroy()
    }

    private fun destroyAdView() {
        mBinding.banner.removeAllViews()
        mBanner?.destroy()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            mBinding.loadBannerButton.id -> {
                loadBanner()
            }
            mBinding.loadInterstitialButton.id -> {
                loadInterstitialAd()
            }
            mBinding.showInterstitialButton.id -> {
                showInterstitialAd()
            }
            mBinding.loadRewardedButton.id -> {
                loadRewarded()
            }
            mBinding.showRewardedButton.id -> {
                showRewardedAd()
            }
            mBinding.toolbar.toolbarUpButton.id -> {
                finish()
            }
        }
    }

    private val mAdListener = object : AdListener() {
        override fun onAdClosed() {
            val event = "Banner: Closed"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val event = "Banner: Load failed $loadAdError"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onAdOpened() {
            val event = "Banner: Opened"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onAdLoaded() {
            val event = "Banner: Loaded"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onAdClicked() {
            val event = "Banner: Clicked"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onAdImpression() {
            val event = "Banner: Impression"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    val event = "Interstitial: Load Success"
                    Log.e(TAG, event)
                    mBinding.interstitialInfo.text = event
                    // Now you are ready to show the interstitial ad
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    val event = "Interstitial: Load Failure $loadAdError"
                    showToast(event)
                    Log.e(TAG, event)
                    mBinding.interstitialInfo.text = event
                    mInterstitialAd = null
                }
            })
    }

    private fun showInterstitialAd() {
        mInterstitialAd?.apply {
            fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        val event = "Interstitial: Show Failure $adError"
                        Log.e(TAG, event)
                        mBinding.interstitialInfo.text = event
                    }

                    override fun onAdShowedFullScreenContent() {
                        val event = "Interstitial: Showed"
                        Log.e(TAG, event)
                        mBinding.interstitialInfo.text = event
                    }

                    override fun onAdDismissedFullScreenContent() {
                        val event = "Interstitial: Dismissed"
                        Log.e(TAG, event)
                        mBinding.interstitialInfo.text = event
                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        val event = "Interstitial: Impression"
                        Log.e(TAG, event)
                        mBinding.interstitialInfo.text = event
                    }

                    override fun onAdClicked() {
                        val event = "Interstitial: Clicked"
                        Log.e(TAG, event)
                        mBinding.interstitialInfo.text = event
                    }
                }
            show(this@AdMobActivity)
        }
    }

    private fun loadRewarded() {
        val adRequest = AdRequest.Builder().build()

        // If you want to specify a user id for the rewarded ad use this request:

        //val extras = AirnowBundleBuilder()
        //    .setUserId("your_user_id")
        //    .build()

        //val adRequest = AdRequest.Builder()
        //    .addNetworkExtrasBundle(AirnowMediationAdapter::class.java, extras)
        //    .build()

        // For test only, replace with your Rewarded ad Unit Id
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
            adRequest, object : RewardedAdLoadCallback() {

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd

                    val event = "Rewarded: Load Success"
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                }
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                    val event = "Rewarded: Load Failure $loadAdError"
                    showToast(event)
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                    mRewardedAd = null
                }
            })
    }

    private fun showRewardedAd() {
        mRewardedAd?.apply {
            fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    val event = "Rewarded: Show Failure $adError"
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                }

                override fun onAdShowedFullScreenContent() {
                    val event = "Rewarded: Showed"
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                }

                override fun onAdDismissedFullScreenContent() {
                    val event = "Rewarded: Dismissed"
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                    mRewardedAd = null
                }

                override fun onAdImpression() {
                    val event = "Rewarded: Impression"
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                }

                override fun onAdClicked() {
                    val event = "Rewarded: Clicked"
                    Log.e(TAG, event)
                    mBinding.rewardedInfo.text = event
                }
            }

            show(this@AdMobActivity) { rewardItem: RewardItem ->
                val event = "Rewarded: Received Reward ${rewardItem.type}: ${rewardItem.amount}"
                showToast(event)
                Log.e(TAG, event)
                mBinding.rewardedInfo.text = event
            }
        }
    }

    private fun loadBanner() {
        mBanner = AdView(this)

        mBanner?.apply {
            adSize = AdSize.BANNER

            // For test only, replace with your Banner ad Unit Id
            adUnitId = "ca-app-pub-3940256099942544/6300978111"

            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            layoutParams = params

            adListener = mAdListener

            mBinding.banner.addView(this)

            val adRequest = AdRequest.Builder().build()
            loadAd(adRequest)
        }
    }
}
