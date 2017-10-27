package pyxis.uzuki.live.sociallogin.line;

import pyxis.uzuki.live.sociallogin.impl.SocialConfig;

/**
 * SocialLogin
 * Class: LineConfig
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class LineConfig extends SocialConfig {
    private String channelId;

    private LineConfig(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public static class Builder {
        private String channelId;

        public Builder setChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public LineConfig build() {
            return new LineConfig(channelId);
        }
    }
}
