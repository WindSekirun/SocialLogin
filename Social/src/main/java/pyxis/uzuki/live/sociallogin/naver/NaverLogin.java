package pyxis.uzuki.live.sociallogin.naver;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.TextureView;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pyxis.uzuki.live.richutilskt.utils.RichUtils;
import pyxis.uzuki.live.sociallogin.SocialLogin;
import pyxis.uzuki.live.sociallogin.impl.OnResponseListener;
import pyxis.uzuki.live.sociallogin.impl.ResultType;
import pyxis.uzuki.live.sociallogin.impl.SocialType;
import pyxis.uzuki.live.sociallogin.impl.UserInfoType;

/**
 * SocialLogin
 * Class: NaverLogin
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class NaverLogin extends SocialLogin {
    private OAuthLogin authLogin = OAuthLogin.getInstance();

    public NaverLogin(Activity activity, OnResponseListener onResponseListener) {
        super(activity, onResponseListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onLogin() {
        NaverConfig config = (NaverConfig) getConfig(SocialType.NAVER);
        authLogin.init(activity, config.getAuthClientId(), config.getAuthClientSecret(), config.getClientName());
        authLogin.startOauthLoginActivity(activity, new NaverLoginHandler());

    }

    @Override
    public void onDestroy() {

    }

    private class NaverLoginHandler extends OAuthLoginHandler {

        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = authLogin.getAccessToken(activity);
                String authHeader = String.format("Bearer %s", accessToken);
                new RequestProfile().execute(authHeader);
            }
        }
    }

    private class RequestProfile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://openapi.naver.com/v1/nid/me")
                        .addHeader("Authorization", strings[0])
                        .build();
                Response response = client.newCall(request).execute();

                return response.body().string();
            } catch (IOException e) {
                responseListener.onResult(SocialType.NAVER, ResultType.FAILURE, null);
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (TextUtils.isEmpty(s)) {
                responseListener.onResult(SocialType.NAVER, ResultType.FAILURE, null);
                return;
            }

            JSONObject jsonObject = RichUtils.createJSONObject(s);
            JSONObject responseObject = RichUtils.getJSONObject(jsonObject, "response");

            Map<UserInfoType, String> userInfoMap = new HashMap<>();

            String id = RichUtils.getJSONString(responseObject, "id");
            String name = RichUtils.getJSONString(responseObject, "name");
            String email = RichUtils.getJSONString(responseObject, "email");

            userInfoMap.put(UserInfoType.ID, id);
            userInfoMap.put(UserInfoType.NAME, name);
            userInfoMap.put(UserInfoType.EMAIL, email);

            responseListener.onResult(SocialType.NAVER, ResultType.SUCCESS, userInfoMap);
        }
    }
}
