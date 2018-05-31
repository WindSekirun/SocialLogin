package pyxis.uzuki.live.sociallogin.facebook;

import android.text.TextUtils;

import java.util.ArrayList;

import pyxis.uzuki.live.sociallogin.impl.SocialConfig;

/**
 * SocialLogin
 * Class: FacebookConfig
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class FacebookConfig extends SocialConfig {
    private ArrayList<String> requestOptions;
    private boolean requireWritePermissions;
    private boolean behaviorOnCancel;
    private FacebookImageEnum mImageEnum = FacebookImageEnum.Large;
    private String applicationId;

    public FacebookConfig(ArrayList<String> requestOptions, boolean requireWritePermissions, boolean behaviorOnCancel, String applicationId) {
        this.requestOptions = requestOptions;
        this.requireWritePermissions = requireWritePermissions;
        this.behaviorOnCancel = behaviorOnCancel;
        this.applicationId = applicationId;
    }

    public ArrayList<String> getRequestOptions() {
        return requestOptions;
    }

    public void setRequestOptions(ArrayList<String> requestOptions) {
        this.requestOptions = requestOptions;
    }

    public boolean isRequireWritePermissions() {
        return requireWritePermissions;
    }

    public void setRequireWritePermissions(boolean requireWritePermissions) {
        this.requireWritePermissions = requireWritePermissions;
    }

    public boolean isBehaviorOnCancel() {
        return behaviorOnCancel;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public FacebookImageEnum getImageEnum() {
        return mImageEnum;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public static class Builder {
        private boolean isRequireEmail = false;
        private boolean isRequireFriends = false;
        private boolean requireWritePermissions = false;
        private boolean behaviorOnCancel = false;
        private String applicationId;
        private FacebookImageEnum imageEnum = FacebookImageEnum.Large;

        public Builder setRequireEmail() {
            isRequireEmail = true;
            return this;
        }

        public Builder setRequireWritePermission() {
            requireWritePermissions = true;
            return this;
        }

        public Builder setApplicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder setRequireFriends() {
            isRequireFriends = true;
            return this;
        }

        public Builder setBehaviorOnCancel() {
            this.behaviorOnCancel = true;
            return this;
        }

        public Builder setPictureSize(FacebookImageEnum imageEnum) {
            this.imageEnum = imageEnum;
            return this;
        }

        public FacebookConfig build() {
            if (TextUtils.isEmpty(applicationId)) {
                throw new IllegalArgumentException("applicationId is empty.");
            }

            ArrayList<String> requestOptions = new ArrayList<>();
            if (isRequireEmail) {
                requestOptions.add("email");
            }

            if (isRequireFriends) {
                requestOptions.add("user_friends");
            }

            requestOptions.add("public_profile");
            return new FacebookConfig(requestOptions, requireWritePermissions, behaviorOnCancel, applicationId);
        }
    }
}
