package pyxis.uzuki.live.sociallogin.google;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

import pyxis.uzuki.live.sociallogin.SocialLogin;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.ResultType;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.impl.UserInfoType;

/**
 * SocialLogin
 * Class: GoogleLogin
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */
public class GoogleLogin extends SocialLogin {
    private static final int REQUEST_CODE_SIGN_IN = 19629;

    private GoogleApiClient mGoogleApiClient;

    public GoogleLogin(AppCompatActivity activity, OnResponseListener onResponseListener) {
        super(activity, onResponseListener);
        GoogleConfig googleConfig = (GoogleConfig) getConfig(SocialType.GOOGLE);

        GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
        if (googleConfig.isRequireEmail()) {
            builder.requestEmail();
        }

        GoogleSignInOptions gso = builder.build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, connectionResult -> {

                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onLogin() {
        if (mGoogleApiClient.isConnected()) mGoogleApiClient.clearDefaultAccountAndReconnect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
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
        if (mGoogleApiClient.isConnected()) mGoogleApiClient.clearDefaultAccountAndReconnect();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            Map<UserInfoType, String> userInfoMaps = new HashMap<>();
            userInfoMaps.put(UserInfoType.NAME, account.getDisplayName());
            userInfoMaps.put(UserInfoType.EMAIL, account.getEmail());
            userInfoMaps.put(UserInfoType.ID, account.getId());
            userInfoMaps.put(UserInfoType.ACCESS_TOKEN, account.getIdToken());

            responseListener.onResult(SocialType.GOOGLE, ResultType.SUCCESS, userInfoMaps);
        } else {
            responseListener.onResult(SocialType.GOOGLE, ResultType.FAILURE, null);
        }
    }
}
