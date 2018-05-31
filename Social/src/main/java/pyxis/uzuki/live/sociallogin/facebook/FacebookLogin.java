package pyxis.uzuki.live.sociallogin.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pyxis.uzuki.live.richutilskt.utils.RichUtils;
import pyxis.uzuki.live.sociallogin.SocialLogin;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.ResultType;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.impl.UserInfoType;

/**
 * SocialLogin
 * Class: FacebookLogin
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class FacebookLogin extends SocialLogin {
    private CallbackManager callbackManager;

    public FacebookLogin(Activity activity, OnResponseListener onResponseListener) {
        super(activity, onResponseListener);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLogin() {
        final FacebookConfig config = (FacebookConfig) getConfig(SocialType.FACEBOOK);

        if (config.isRequireWritePermissions()) {
            LoginManager.getInstance().logInWithPublishPermissions(activity, config.getRequestOptions());
        } else {
            LoginManager.getInstance().logInWithReadPermissions(activity, config.getRequestOptions());
        }

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserInfo();
            }

            @Override
            public void onCancel() {
                if (config.isBehaviorOnCancel()) {
                    getUserInfo();
                } else {
                    responseListener.onResult(SocialType.FACEBOOK, ResultType.FAILURE, null);
                }
            }

            @Override
            public void onError(FacebookException error) {
                responseListener.onResult(SocialType.FACEBOOK, ResultType.FAILURE, null);
            }
        });
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
        LoginManager.getInstance().logOut();
    }

    private void getUserInfo() {
        final FacebookConfig config = (FacebookConfig) getConfig(SocialType.FACEBOOK);

        GraphRequest.GraphJSONObjectCallback callback = (object, response) -> {
            if (object == null) {
                responseListener.onResult(SocialType.FACEBOOK, ResultType.FAILURE, null);
                return;
            }

            String id = RichUtils.getJSONString(object, "id");
            String name = RichUtils.getJSONString(object, "name");
            String email = RichUtils.getJSONString(object, "email");
            String gender = RichUtils.getJSONString(object, "gender");
            String firstName = RichUtils.getJSONString(object, "first_name");
            JSONObject data = RichUtils.getJSONObject(RichUtils.getJSONObject(object, "picture"), "data");
            String profilePicture = RichUtils.getJSONString(data, "url");

            Map<UserInfoType, String> userInfoMap = new HashMap<>();
            userInfoMap.put(UserInfoType.ID, id);
            userInfoMap.put(UserInfoType.NAME, name);
            userInfoMap.put(UserInfoType.EMAIL, email);
            userInfoMap.put(UserInfoType.PROFILE_PICTURE, profilePicture);
            userInfoMap.put(UserInfoType.GENDER, gender);
            userInfoMap.put(UserInfoType.FIRST_NAME, firstName);

            responseListener.onResult(SocialType.FACEBOOK, ResultType.SUCCESS, userInfoMap);
        };

        String originField = "id, name, email, gender, birthday, first_name, ";
        originField += config.getImageEnum().getFieldName();

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", originField);
        request.setParameters(parameters);
        request.executeAsync();
    }
}
