package pyxis.uzuki.live.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.annimon.stream.Stream;
import com.facebook.FacebookSdk;
import com.kakao.auth.KakaoSDK;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.List;

import pyxis.uzuki.live.sociallogin.facebook.FacebookConfig;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.kakao.KakaoSDKAdapter;
import pyxis.uzuki.live.sociallogin.impl.SocialConfig;
import pyxis.uzuki.live.sociallogin.impl.SocialLoginType;
import pyxis.uzuki.live.sociallogin.twitter.TwitterConfig;

/**
 * SocialLogin
 * Class: SocialLogin
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public abstract class SocialLogin {
    protected Activity activity;
    protected OnResponseListener responseListener;
    private static List<SocialLoginType> availableList;

    public SocialLogin(Activity activity, OnResponseListener onResponseListener) {
        this.activity = activity;
        responseListener = onResponseListener;
    }

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void onLogin();

    public abstract void onDestroy();

    public static void init(Context context, List<SocialLoginType> availableType) {
        availableList = availableType;

        for (SocialLoginType type : availableType) {
            switch (type.getType()) {
                case KAKAO:
                    initializeKakaoSDK(context);
                    break;
                case GOOGLE:
                    break;
                case TWITTER:
                    initializeTwitterSDK(context, (TwitterConfig) getConfig(SocialType.TWITTER));
                    break;
                case FACEBOOK:
                    initializeFacebookSDK(context, (FacebookConfig) getConfig(SocialType.FACEBOOK));
                    break;
            }
        }
    }

    protected static SocialConfig getConfig(SocialType type) {
        return Stream.of(availableList)
                .filter(value -> value.getType() == type)
                .findSingle()
                .get()
                .getConfig();
    }

    private static void initializeKakaoSDK(Context context) {
        KakaoSDK.init(new KakaoSDKAdapter(context));
    }

    private static void initializeFacebookSDK(Context context, FacebookConfig config) {
        FacebookSdk.setApplicationId(config.getApplicationId());
        FacebookSdk.sdkInitialize(context);
    }

    private static void initializeTwitterSDK(Context context, TwitterConfig config) {
        com.twitter.sdk.android.core.TwitterConfig twitterConfig = new com.twitter.sdk.android.core.TwitterConfig.Builder(context)
                .twitterAuthConfig(new TwitterAuthConfig(config.getConsumerKey(), config.getConsumerSecret()))
                .build();

        Twitter.initialize(twitterConfig);
    }
}
