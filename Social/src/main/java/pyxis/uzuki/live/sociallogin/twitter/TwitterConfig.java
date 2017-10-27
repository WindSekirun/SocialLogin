package pyxis.uzuki.live.sociallogin.twitter;

import pyxis.uzuki.live.sociallogin.impl.SocialConfig;

/**
 * SocialLogin
 * Class: TwitterConfig
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class TwitterConfig extends SocialConfig {
    private String consumerKey;
    private String consumerSecret;

    private TwitterConfig(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public static class Builder {
        private String consumerKey;
        private String consumerSecret;

        public Builder setConsumerKey(String consumerKey) {
            this.consumerKey = consumerKey;
            return this;
        }

        public Builder setConsumerSecret(String consumerSecret) {
            this.consumerSecret = consumerSecret;
            return this;
        }

        public TwitterConfig build() {
            return new TwitterConfig(consumerKey, consumerSecret);
        }
    }
}
