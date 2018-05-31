package pyxis.uzuki.live.sociallogin.kakao;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.util.HashMap;
import java.util.Map;

import pyxis.uzuki.live.sociallogin.SocialLogin;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.ResultType;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.impl.UserInfoType;

/**
 * SocialLogin
 * Class: KakaoLogin
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class KakaoLogin extends SocialLogin {
    private SessionCallback mSessionCallback;

    public KakaoLogin(Activity activity, OnResponseListener onResponseListener) {
        super(activity, onResponseListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLogin() {
        Session.getCurrentSession().removeCallback(mSessionCallback);
        mSessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mSessionCallback);
        if (!Session.getCurrentSession().checkAndImplicitOpen())
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, activity);
    }

    @Override
    public void onDestroy() {
        if (mSessionCallback != null)
            Session.getCurrentSession().removeCallback(mSessionCallback);
    }

    @Override
    public void logout() {
        logout(false);
    }

    @Override
    public void logout(boolean clearToken) {
        if (Session.getCurrentSession().checkAndImplicitOpen())
            Session.getCurrentSession().close();
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("SessionCallback", String.format("OpenFailed:: %s", exception != null ? exception.getMessage() : ""));
        }
    }

    void requestMe() {
        KakaoConfig config = (KakaoConfig) getConfig(SocialType.KAKAO);

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                responseListener.onResult(SocialType.KAKAO, ResultType.FAILURE, null);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                responseListener.onResult(SocialType.KAKAO, ResultType.FAILURE, null);
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String id = String.valueOf(userProfile.getId());
                String nickname = !TextUtils.isEmpty(userProfile.getNickname()) ? userProfile.getNickname() : "";
                String email = userProfile.getEmail();
                String profilePicture = !TextUtils.isEmpty(userProfile.getProfileImagePath()) ? userProfile.getProfileImagePath() : "";
                String thumbnailPicture = !TextUtils.isEmpty(userProfile.getThumbnailImagePath()) ? userProfile.getThumbnailImagePath() : "";
                boolean isEmailVerified = userProfile.getEmailVerified();

                Map<UserInfoType, String> userInfoMap = new HashMap<>();
                userInfoMap.put(UserInfoType.ID, id);
                userInfoMap.put(UserInfoType.NICKNAME, nickname);
                userInfoMap.put(UserInfoType.EMAIL, email);
                userInfoMap.put(UserInfoType.PROFILE_PICTURE, profilePicture);
                userInfoMap.put(UserInfoType.EMAIL_VERIFIED, String.valueOf(isEmailVerified));
                userInfoMap.put(UserInfoType.THUMBNAIL_IMAGE, thumbnailPicture);

                responseListener.onResult(SocialType.KAKAO, ResultType.SUCCESS, userInfoMap);
            }

            @Override
            public void onNotSignedUp() {

            }
        }, config.getRequestOptions(), config.isSecureResource());
    }
}
