package com.airnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.airnow.databinding.ActivityMainBinding
import android.content.Intent




class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)

        mBinding.directAds.setOnClickListener(this)
        mBinding.admobMediation.setOnClickListener(this)
        mBinding.mopubMediation.setOnClickListener(this)

        mBinding.sdkVersion.text = getString(R.string.sdk_version, AirnowSdk.getSDKVersion())
        mBinding.appVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            mBinding.directAds.id -> {
                val intent = Intent(this, AirnowMediaActivity::class.java)
                startActivity(intent)
            }
            mBinding.admobMediation.id -> {
                val intent = Intent(this, AdMobActivity::class.java)
                startActivity(intent)
            }
            mBinding.mopubMediation.id -> {
                val intent = Intent(this, MoPubActivity::class.java)
                startActivity(intent)
            }
        }
    }
}