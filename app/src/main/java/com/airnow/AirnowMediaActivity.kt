package com.airnow

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.airnow.databinding.ActivityAirnowMediaBinding
import com.airnow.internal.ads.types.rewarded.AirnowReward

class AirnowMediaActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = AirnowMediaActivity::class.java.simpleName

    private lateinit var mBinding: ActivityAirnowMediaBinding

    private var mAirnowAppBanner: AirnowAppBanner? = null
    private var mAirnowBanner: AirnowBanner? = null
    private var mAirnowInterstitial: AirnowInterstitial? = null
    private var mAirnowRewarded: AirnowRewarded? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Replace for your apiKey and appId
        AirnowSdk.init(this, "1638978300298486440", "471710")

        // Set Airnow SDK test mode
        AirnowSdk.setTestMode(true)

        // Regulation Advanced Settings

        // GDPR - Managing Consent
        //AirnowSdk.setGdprConsent(AirnowConsent.ACCEPTED)

        // CCPA Compliance
        //AirnowSdk.setCcpaConsent(true)

        // User-Level Settings for Child-Directed Apps with Age Gates
        //AirnowSdk.setChildDirected(false)

        // Whether or not the SDK is allowed to use personalized ads.
        //AirnowSdk.setOptOut(this, false)


        mBinding = ActivityAirnowMediaBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)

        mBinding.loadInAppButton.setOnClickListener(this)
        mBinding.loadInlineButton.setOnClickListener(this)
        mBinding.loadInterstitialButton.setOnClickListener(this)
        mBinding.showInterstitialButton.setOnClickListener(this)
        mBinding.loadRewardedButton.setOnClickListener(this)
        mBinding.showRewardedButton.setOnClickListener(this)


        mBinding.toolbar.toolbarUpButton.setOnClickListener(this)
        mBinding.toolbar.toolbarTitle.text = getString(R.string.title_direct_ads)
        mBinding.sdkVersion.text = getString(R.string.sdk_version, AirnowSdk.getSDKVersion())
        mBinding.appVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }

    override fun onDestroy() {
        destroyAdView()
        super.onDestroy()
    }

    private fun destroyAdView() {
        mBinding.inlineBanner.removeAllViews()
        mAirnowBanner?.destroy()
        mAirnowAppBanner?.destroy()
        mAirnowInterstitial?.destroy()
        mAirnowRewarded?.destroy()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            mBinding.loadInAppButton.id -> {
                loadInAppBanner()
            }
            mBinding.loadInlineButton.id -> {
                loadInlineBanner()
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

    private val mAirnowInterstitialAdListener = object : AirnowInterstitialAdListener {

        override fun onAdLoadSuccess() {
            val event = "Interstitial: Load Success"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
            // Now you are ready to show the interstitial ad
        }

        override fun onAdLoadFailure(reason: String?) {
            val event = "Interstitial: Load Failure\n${reason ?: "loading error"}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onAdClicked() {
            val event = "Interstitial: Clicked"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }
        override fun onAdOpened() {
            val event = "Interstitial: Opened"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }
        override fun onAdClosed() {
            val event = "Interstitial: Closed"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }
        override fun onAdLeaveApplication() {
            val event = "Interstitial: Leave Application"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onAdCompleted() {
            val event = "Interstitial: Completed"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onAdShowFailure(reason: String?) {
            val event = "Interstitial: Show Failure ${reason ?: "showing error"}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }
    }

    private val mAirnowRewardedAdListener = object : AirnowRewardedAdListener {
        override fun onAdLoadSuccess() {
            val event = "Rewarded: Load Success"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
            // Now you are ready to show the interstitial ad
        }

        override fun onAdLoadFailure(reason: String?) {
            val event = "Rewarded: Load Failure ${reason ?: "loading error"}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onAdClicked() {
            val event = "Rewarded: Clicked"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }
        override fun onAdOpened() {
            val event = "Rewarded: Opened"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }
        override fun onAdClosed() {
            val event = "Rewarded: Closed"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }
        override fun onAdLeaveApplication() {
            val event = "Rewarded: Leave Application"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onAdCompleted() {
            val event = "Rewarded: Completed"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onAdShowFailure(reason: String?) {
            val event = "Rewarded: Show Failure ${reason ?: "showing error"}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onReceivedReward(reward: AirnowReward?) {
            val event = "Rewarded: Received Reward ${reward?.label}: ${reward?.amount}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onRewardedAdStarted() {
            val event = "Rewarded: Started"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

    }

    private val mAirnowInAppBannerAdListener = object : AirnowBannerAdListener {
        override fun onAdLoadSuccess() {
            val event = "In-App banner: Load Success"
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
            // Now you are ready to show the interstitial ad
        }

        override fun onAdLoadFailure(reason: String?) {
            val event = "In-App banner: Load Failure ${reason ?: "loading error"}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
        }

        override fun onAdClicked() {
            val event = "In-App banner: Clicked"
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
        }
        override fun onAdOpened() {
            val event = "In-App banner: Opened"
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
        }
        override fun onAdClosed() {
            val event = "In-App banner: Closed"
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
        }
        override fun onAdLeaveApplication() {
            val event = "In-App banner: Leave Application"
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
        }

        override fun onAdCompleted() {
            val event = "In-App banner: Completed"
            Log.e(TAG, event)
            mBinding.inAppBannerInfo.text = event
        }
    }

    private val mAirnowBannerAdListener = object : AirnowBannerAdListener {
        override fun onAdLoadSuccess() {
            val event = "Inline banner: Load Success"
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
            // Now you are ready to show the interstitial ad
        }

        override fun onAdLoadFailure(reason: String?) {
            val event = "Inline banner: Load Failure ${reason ?: "loading error"}"
            showToast(event)
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
        }

        override fun onAdClicked() {
            val event = "Inline banner: Clicked"
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
        }
        override fun onAdOpened() {
            val event = "Inline banner: Opened"
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
        }
        override fun onAdClosed() {
            val event = "Inline banner: Closed"
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
        }
        override fun onAdLeaveApplication() {
            val event = "Inline banner: Leave Application"
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
        }

        override fun onAdCompleted() {
            val event = "Inline banner: Completed"
            Log.e(TAG, event)
            mBinding.inlineBannerInfo.text = event
        }
    }

    private fun loadInterstitialAd() {
        mAirnowInterstitial = AirnowInterstitial(this)
        // mAirnowInterstitial = AirnowInterstitial(this, "your_interstitial_placement")
        mAirnowInterstitial?.apply {
            setInterstitialAdListener(mAirnowInterstitialAdListener)
            load()
        }
    }

    private fun showInterstitialAd() {
        mAirnowInterstitial?.apply {
            if (isLoaded) show()
        }
    }

    private fun loadRewarded() {
        // Replace for your rewarded placement
        mAirnowRewarded = AirnowRewarded(this, "Home_Screen")
        mAirnowRewarded?.apply {
            // setUserId("your_user_id")
            setRewardedAdListener(mAirnowRewardedAdListener)
            load()
        }
    }

    private fun showRewardedAd() {
        mAirnowRewarded?.apply {
            if (isLoaded) show()
        }
    }

    private fun loadInAppBanner() {
        mAirnowAppBanner = AirnowAppBanner(this)

        mAirnowAppBanner?.apply {
            // Set max banner size
            setSize(320, 50)
            // setPlacementName("your_banner_placement")
            setBannerAdListener(mAirnowInAppBannerAdListener)
            load()
        }
    }

    private fun loadInlineBanner() {
        mAirnowBanner = AirnowBanner(this)

        mAirnowBanner?.apply {
            // Set max banner size
            setSize(320, 50)

            gravity = Gravity.CENTER_HORIZONTAL

            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            layoutParams = params

            setBannerAdListener(mAirnowBannerAdListener)

            mBinding.inlineBanner.addView(this)

            load()
        }
    }
}
