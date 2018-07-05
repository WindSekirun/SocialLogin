package pyxis.uzuki.live.sociallogin.kakao;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
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

    private void requestMe() {
        KakaoConfig config = (KakaoConfig) getConfig(SocialType.KAKAO);

        UserManagement.getInstance().me(config.getRequestOptions(), new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                responseListener.onResult(SocialType.KAKAO, ResultType.FAILURE, null);
            }

            @Override
            public void onSuccess(MeV2Response result) {
                // default value
                String id = String.valueOf(result.getId());
                String nickname = result.getNickname();
                String profilePicture = result.getProfileImagePath();
                String thumbnailPicture = result.getThumbnailImagePath();

                Map<UserInfoType, String> userInfoMap = new HashMap<>();
                userInfoMap.put(UserInfoType.ID, id);
                userInfoMap.put(UserInfoType.NICKNAME, nickname);
                userInfoMap.put(UserInfoType.PROFILE_PICTURE, profilePicture);
                userInfoMap.put(UserInfoType.THUMBNAIL_IMAGE, thumbnailPicture);

                // optional value
                String email = "";
                String gender = "";
                String ageRange = "";
                String birthday = "";
                boolean isEmailVerified = false;
                UserAccount userAccount = result.getKakaoAccount();

                if (userAccount != null && userAccount.hasEmail() == OptionalBoolean.TRUE) {
                    email = userAccount.getEmail();
                    isEmailVerified = userAccount.isEmailVerified() == OptionalBoolean.TRUE;
                }

                if (userAccount != null && userAccount.hasAgeRange() == OptionalBoolean.TRUE) {
                    ageRange = userAccount.getAgeRange().getValue();
                }

                if (userAccount != null && userAccount.hasGender() == OptionalBoolean.TRUE) {
                    gender = userAccount.getGender().getValue();
                }

                if (userAccount != null && userAccount.hasBirthday() == OptionalBoolean.TRUE) {
                    birthday = userAccount.getBirthday();
                }

                userInfoMap.put(UserInfoType.EMAIL, email);
                userInfoMap.put(UserInfoType.EMAIL_VERIFIED, String.valueOf(isEmailVerified));
                userInfoMap.put(UserInfoType.GENDER, gender);
                userInfoMap.put(UserInfoType.AGE_RANGE, ageRange);
                userInfoMap.put(UserInfoType.BIRTHDAY, birthday);

                responseListener.onResult(SocialType.KAKAO, ResultType.SUCCESS, userInfoMap);
            }
        });
    }
}
