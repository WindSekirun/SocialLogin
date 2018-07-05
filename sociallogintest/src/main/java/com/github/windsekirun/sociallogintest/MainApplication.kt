package com.github.windsekirun.sociallogintest

import android.app.Application

import pyxis.uzuki.live.sociallogin.SocialLogin
import pyxis.uzuki.live.sociallogin.impl.SocialType
import pyxis.uzuki.live.sociallogin.kakao.KakaoConfig

/**
 * SocialLogin
 * Class: MainApplication
 * Created by Pyxis on 7/2/18.
 *
 *
 * Description:
 */

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SocialLogin.init(this)
        val kakaoConfig = KakaoConfig.Builder()
                .setRequireEmail()
                .setRequireAgeRange()
                .setRequireBirthday()
                .setRequireEmail()
                .setRequireGender()
                .build()

        SocialLogin.addType(SocialType.KAKAO, kakaoConfig)
    }
}
