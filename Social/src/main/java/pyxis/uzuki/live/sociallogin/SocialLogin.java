package pyxis.uzuki.live.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.kakao.auth.KakaoSDK;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.HashMap;
import java.util.Map;

import pyxis.uzuki.live.sociallogin.facebook.FacebookConfig;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.SocialConfig;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.kakao.KakaoSDKAdapter;
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
    private static Map<SocialType, SocialConfig> availableTypeMap = new HashMap<>();
    private static Context mContext;

    public SocialLogin(Activity activity, OnResponseListener onResponseListener) {
        this.activity = activity;
        responseListener = onResponseListener;
    }

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void onLogin();

    public abstract void onDestroy();

    public static void init(Context context) {
        mContext = context;
        availableTypeMap.clear();
    }

    public static void addType(SocialType socialType, SocialConfig socialConfig) {
        availableTypeMap.put(socialType, socialConfig);
        switch (socialType) {
            case KAKAO:
                initializeKakaoSDK(mContext);
                break;
            case TWITTER:
                initializeTwitterSDK(mContext, (TwitterConfig) socialConfig);
                break;
            case FACEBOOK:
                initializeFacebookSDK(mContext, (FacebookConfig) socialConfig);
                break;
        }
    }

    public static void removeType(SocialType socialType) {
        availableTypeMap.remove(socialType);
    }

    protected static SocialConfig getConfig(SocialType type) {
        if (!availableTypeMap.containsKey(type)) {
            throw new IllegalStateException(String.format("No config is available, please add proper config :: SocialType -> %s", type.name()));
        }
        return availableTypeMap.get(type);
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
