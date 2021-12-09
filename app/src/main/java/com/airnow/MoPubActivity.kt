package com.airnow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.airnow.databinding.ActivityMobpubBinding
import com.mopub.common.MoPub
import com.mopub.common.MoPubReward
import com.mopub.common.SdkConfiguration
import com.mopub.common.logging.MoPubLog
import com.mopub.mobileads.*
import java.util.HashMap

class MoPubActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = MoPubActivity::class.java.simpleName

    private lateinit var mBinding: ActivityMobpubBinding

    private var mBanner: MoPubView? = null
    private var mInterstitialAd: MoPubInterstitial? = null
    private var mRewardedAdReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the MoPub Ads SDK.
        initMoPubSDK(this)

        // Set Airnow Media SDK test mode
        AirnowSdk.setTestMode(true)

        mBinding = ActivityMobpubBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)

        mBinding.loadBannerButton.setOnClickListener(this)
        mBinding.loadInterstitialButton.setOnClickListener(this)
        mBinding.showInterstitialButton.setOnClickListener(this)
        mBinding.loadRewardedButton.setOnClickListener(this)
        mBinding.showRewardedButton.setOnClickListener(this)


        mBinding.toolbar.toolbarUpButton.setOnClickListener(this)
        mBinding.toolbar.toolbarTitle.text = getString(R.string.title_mopub_mediation)
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
        mInterstitialAd?.destroy()
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

    private val mBannerAdListener = object : MoPubView.BannerAdListener {
        override fun onBannerLoaded(banner: MoPubView) {
            val event = "Banner: Loaded"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onBannerFailed(banner: MoPubView, errorCode: MoPubErrorCode) {
            val event = "Banner: Load failed $errorCode"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onBannerClicked(banner: MoPubView) {
            val event = "Banner: Clicked"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }

        override fun onBannerExpanded(banner: MoPubView) {
            val event = "Banner: Expanded"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }
        override fun onBannerCollapsed(banner: MoPubView) {
            val event = "Banner: Collapsed"
            Log.e(TAG, event)
            mBinding.bannerInfo.text = event
        }
    }

    private val mInterstitialAdListener = object : MoPubInterstitial.InterstitialAdListener {

        override fun onInterstitialLoaded(interstitialAd: MoPubInterstitial) {
            val event = "Interstitial: Loaded"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onInterstitialFailed(
            interstitialAd: MoPubInterstitial,
            errorCode: MoPubErrorCode
        ) {
            val event = "Interstitial: Load failed $errorCode"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onInterstitialShown(interstitial1: MoPubInterstitial) {
            val event = "Interstitial: Shown"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onInterstitialClicked(interstitial1: MoPubInterstitial) {
            val event = "Interstitial: Clicked"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }

        override fun onInterstitialDismissed(interstitial1: MoPubInterstitial) {
            val event = "Interstitial: Dismissed"
            Log.e(TAG, event)
            mBinding.interstitialInfo.text = event
        }
    }

    private val mRewardedAdListener = object : MoPubRewardedAdListener {
        override fun onRewardedAdLoadSuccess(adUnitId: String) {
            val event = "Rewarded: Loaded"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
            mRewardedAdReady = true
        }

        override fun onRewardedAdLoadFailure(adUnitId: String, errorCode: MoPubErrorCode) {
            val event = "Rewarded: Load failed $errorCode"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onRewardedAdStarted(adUnitId: String) {
            val event = "Rewarded: Startedd"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onRewardedAdShowError(adUnitId: String, errorCode: MoPubErrorCode) {
            val event = "Rewarded: Show failed $errorCode"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onRewardedAdClicked(adUnitId: String) {
            val event = "Rewarded: Clicked"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onRewardedAdClosed(adUnitId: String) {
            val event = "Rewarded: Closed"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }

        override fun onRewardedAdCompleted(adUnitIds: Set<String?>, reward: MoPubReward) {
            val event = "Rewarded: Completed"
            Log.e(TAG, event)
            mBinding.rewardedInfo.text = event
        }
    }

    private fun loadInterstitialAd() {

        mInterstitialAd = MoPubInterstitial(this, "24534e1901884e398f1253216226017e")

        mInterstitialAd?.apply {

            interstitialAdListener = mInterstitialAdListener

            load()
        }
    }

    private fun showInterstitialAd() {
        mInterstitialAd?.apply {
            if (isReady) show()
        }
    }

    private fun loadRewarded() {
        MoPubRewardedAds.setRewardedAdListener(mRewardedAdListener)

        // Use your ad unit ID for Rewarded
        MoPubRewardedAds.loadRewardedAd("920b6145fb1546cf8b5cf2ac34638bb7")
    }

    private fun showRewardedAd() {
        if (mRewardedAdReady) {
            MoPubRewardedAds.showRewardedAd(
                "920b6145fb1546cf8b5cf2ac34638bb7"
            )
        }
    }

    private fun loadBanner() {
        mBanner = MoPubView(this)

        mBanner?.apply {

            // Use your ad unit ID for Banner
            setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da")

            adSize = MoPubView.MoPubAdSize.HEIGHT_50

            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            layoutParams = params

            bannerAdListener = mBannerAdListener

            mBinding.banner.addView(this)

            loadAd()
        }
    }

    private fun initMoPubSDK(context: Context) {
        // Use using any valid ad unit ID that belongs to the specific app you’re initializing with MoPub.
        val configBuilder = SdkConfiguration.Builder(
            "b195f8dd8ded45fe847ad89ed1d016da"
        )
        if (BuildConfig.DEBUG) {
            configBuilder.withLogLevel(MoPubLog.LogLevel.DEBUG)
        } else {
            configBuilder.withLogLevel(MoPubLog.LogLevel.INFO)
        }

        val airnowMediaConfiguration: MutableMap<String, String> = HashMap()

        // Set your apiKey and appId for initialization Airnow Media SDK
        airnowMediaConfiguration[AirnowAdapterConfiguration.AIRNOW_API_KEY] = "1512296951303967283"
        airnowMediaConfiguration[AirnowAdapterConfiguration.AIRNOW_APP_ID] = "384198"

        // You can specify a user id for the rewarded ad
        //airnowMediaConfiguration[AirnowAdapterConfiguration.AIRNOW_REWARDED_USER_ID] = "you_user_id"

        configBuilder.withAdditionalNetwork(AirnowAdapterConfiguration::class.java.name)
        configBuilder.withMediatedNetworkConfiguration(
            AirnowAdapterConfiguration::class.java.name,
            airnowMediaConfiguration
        )

        MoPub.initializeSdk(context, configBuilder.build()) { }
    }
}
