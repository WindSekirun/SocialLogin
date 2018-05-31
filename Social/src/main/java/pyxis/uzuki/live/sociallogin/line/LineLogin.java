package pyxis.uzuki.live.sociallogin.line;

import android.app.Activity;
import android.content.Intent;

import com.linecorp.linesdk.LineProfile;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import java.util.HashMap;
import java.util.Map;

import pyxis.uzuki.live.sociallogin.SocialLogin;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.ResultType;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.impl.UserInfoType;

/**
 * SocialLogin
 * Class: LineLogin
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */
public class LineLogin extends SocialLogin {
    private static final int REQUEST_CODE = 8073;

    public LineLogin(Activity activity, OnResponseListener onResponseListener) {
        super(activity, onResponseListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) onResultLineLogin(data);
    }

    @Override
    public void onLogin() {
        LineConfig lineConfig = (LineConfig) getConfig(SocialType.LINE);
        Intent loginIntent = LineLoginApi.getLoginIntent(activity, lineConfig.getChannelId());
        activity.startActivityForResult(loginIntent, REQUEST_CODE);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void logout() {
        logout(false);
    }

    @Override
    public void logout(boolean clearToken) {

    }

    private void onResultLineLogin(Intent data) {
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {
            case SUCCESS:
                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();
                LineProfile lineProfile = result.getLineProfile();
                if (lineProfile == null) {
                    responseListener.onResult(SocialType.LINE, ResultType.FAILURE, null);
                    return;
                }

                Map<UserInfoType, String> userInfoMap = new HashMap<>();
                userInfoMap.put(UserInfoType.ACCESS_TOKEN, accessToken);
                userInfoMap.put(UserInfoType.ID, lineProfile.getUserId());
                userInfoMap.put(UserInfoType.NAME, lineProfile.getDisplayName());

                responseListener.onResult(SocialType.LINE, ResultType.SUCCESS, userInfoMap);
                break;

            case CANCEL:
                responseListener.onResult(SocialType.LINE, ResultType.FAILURE, null);
                break;
            case NETWORK_ERROR:
            case SERVER_ERROR:
            case AUTHENTICATION_AGENT_ERROR:
            case INTERNAL_ERROR:
                responseListener.onResult(SocialType.LINE, ResultType.FAILURE, null);
                break;
        }
    }
}
