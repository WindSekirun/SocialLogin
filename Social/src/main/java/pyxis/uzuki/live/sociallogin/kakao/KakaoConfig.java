package pyxis.uzuki.live.sociallogin.kakao;

import java.io.Serializable;
import java.util.ArrayList;

import pyxis.uzuki.live.sociallogin.impl.SocialConfig;

/**
 * SocialLogin
 * Class: KakaoConfig
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class KakaoConfig extends SocialConfig implements Serializable {
    private ArrayList<String> requestOptions;
    private boolean isSecureResource = false;

    private KakaoConfig(ArrayList<String> requestOptions, boolean isSecureResource) {
        this.requestOptions = requestOptions;
        this.isSecureResource = isSecureResource;
    }

    public ArrayList<String> getRequestOptions() {
        return requestOptions;
    }

    public boolean isSecureResource() {
        return isSecureResource;
    }

    public static class Builder {
        private boolean isRequireEmail = false;
        private boolean isRequireNickname = false;
        private boolean isSecureResource = false;

        public Builder setRequireEmail() {
            isRequireEmail = true;
            return this;
        }

        public Builder setRequireNickname() {
            isRequireNickname = true;
            return this;
        }

        public Builder setSecureResource() {
            isSecureResource = true;
            return this;
        }

        public KakaoConfig build() {
            ArrayList<String> requestOptions = new ArrayList<>();
            if (isRequireEmail) {
                requestOptions.add("kaccount_email");
            }

            if (isRequireNickname) {
                requestOptions.add("nickname");
            }

            return new KakaoConfig(requestOptions, isSecureResource);
        }
    }
}
