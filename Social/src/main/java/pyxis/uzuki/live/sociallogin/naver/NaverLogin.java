package pyxis.uzuki.live.sociallogin.naver;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

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

            /*
            resultcode	String	Y	API 호출 결과 코드
            message	String	Y	호출 결과 메시지
            response/id	String	Y	동일인 식별 정보
            동일인 식별 정보는 네이버 아이디마다 고유하게 발급되는 값입니다.
            response/nickname	String	Y	사용자 별명
            response/name	String	Y	사용자 이름
            response/email	String	Y	사용자 메일 주소
            response/gender	String	Y	성별
                                        - F: 여성
                                        - M: 남성
                                        - U: 확인불가
            response/age	String	Y	사용자 연령대
            response/birthday	String	Y	사용자 생일(MM-DD 형식)
            response/profile_image	String	Y	사용자 프로필 사진 URL
             */

            String id = RichUtils.getJSONString(responseObject, "id");
            String name = RichUtils.getJSONString(responseObject, "name");
            String email = RichUtils.getJSONString(responseObject, "email");
            String nickname = RichUtils.getJSONString(responseObject, "nickname");
            String profileImage = RichUtils.getJSONString(responseObject, "profile_image");

            userInfoMap.put(UserInfoType.ID, id);
            userInfoMap.put(UserInfoType.NAME, name);
            userInfoMap.put(UserInfoType.EMAIL, email);
            userInfoMap.put(UserInfoType.NICKNAME, nickname);
            userInfoMap.put(UserInfoType.PROFILE_PICTRUE, profileImage);

            responseListener.onResult(SocialType.NAVER, ResultType.SUCCESS, userInfoMap);
        }
    }
}
