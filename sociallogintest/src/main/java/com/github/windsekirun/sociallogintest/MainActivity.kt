package com.github.windsekirun.sociallogintest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import pyxis.uzuki.live.richutilskt.utils.getKeyHash
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener
import pyxis.uzuki.live.sociallogin.kakao.KakaoLogin

class MainActivity : AppCompatActivity() {
    private lateinit var kakaoLogin: KakaoLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(MainActivity::class.java.simpleName, "KeyHash: ${getKeyHash()}," +
                " apiKey: ${getString(R.string.kakao_api_key)}")

        btnLogin.setOnClickListener {
            if (::kakaoLogin.isInitialized) {
                kakaoLogin.onLogin()
            }
        }

        kakaoLogin = KakaoLogin(this, OnResponseListener { _, result, resultMap ->
            val typeStr = "type: ${result?.name}"
            val resultMapStr = toMapString(resultMap)

            Log.d(MainActivity::class.java.simpleName, resultMapStr);
            txtResult.text = "$typeStr \n $resultMapStr"
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::kakaoLogin.isInitialized) {
            kakaoLogin.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::kakaoLogin.isInitialized) {
            kakaoLogin.onDestroy()
        }
    }

    @JvmOverloads
    fun <K, V> toMapString(map: Map<K, V>, delimiter: CharSequence = "\n"): String {
        val builder = StringBuilder()
        val lists = map.entries.toList()
        (0 until lists.size)
                .map { lists[it] }
                .forEach { builder.append("[${it.key}] -> [${it.value}]$delimiter") }
        return builder.toString()
    }

}
