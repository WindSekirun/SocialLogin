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

    private KakaoConfig(ArrayList<String> requestOptions) {
        this.requestOptions = requestOptions;
    }

    public ArrayList<String> getRequestOptions() {
        return requestOptions;
    }

    public static class Builder {
        private boolean isRequireEmail = false;
        private boolean isRequireNickname = false;
        private boolean isRequireImage = false;
        private boolean isRequireAgeRange = false;
        private boolean isRequireBirthday = false;
        private boolean isRequireGender = false;

        public Builder setRequireEmail() {
            isRequireEmail = true;
            return this;
        }

        public Builder setRequireNickname() {
            isRequireNickname = true;
            return this;
        }

        public Builder setRequireImage() {
            isRequireImage = true;
            return this;
        }

        public Builder setRequireAgeRange() {
            isRequireAgeRange = true;
            return this;
        }

        public Builder setRequireBirthday() {
            isRequireBirthday = true;
            return this;
        }

        public Builder setRequireGender() {
            isRequireGender = true;
            return this;
        }

        public KakaoConfig build() {
            // v 1.2.5 migrate with V1 -> V2
            // according to https://tinyurl.com/ycaf5yua
            ArrayList<String> requestOptions = new ArrayList<>();
            if (isRequireEmail) {
                requestOptions.add("kakao_account.email");
            }

            if (isRequireNickname) {
                requestOptions.add("properties.nickname");
            }

            if (isRequireImage) {
                requestOptions.add("properties.profile_image");
                requestOptions.add("properties.thumbnail_image");
            }

            if (isRequireAgeRange) {
                requestOptions.add("kakao_account.age_range");
            }

            if (isRequireBirthday) {
                requestOptions.add("kakao_account.birthday");
            }

            if (isRequireGender) {
                requestOptions.add("kakao_account.gender");
            }

            return new KakaoConfig(requestOptions);
        }
    }
}
