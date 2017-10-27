package pyxis.uzuki.live.sociallogin.google;

import pyxis.uzuki.live.sociallogin.impl.SocialConfig;

/**
 * SocialLogin
 * Class: GoogleConfig
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class GoogleConfig extends SocialConfig {
    private boolean requireEmail = false;

    private GoogleConfig(boolean requireEmail) {
        this.requireEmail = requireEmail;
    }

    public boolean isRequireEmail() {
        return requireEmail;
    }

    public static class Builder {
        private boolean requireEmail = false;

        public Builder setRequireEmail() {
            requireEmail = true;
            return this;
        }

        public GoogleConfig build() {
            return new GoogleConfig(requireEmail);
        }
    }
}
