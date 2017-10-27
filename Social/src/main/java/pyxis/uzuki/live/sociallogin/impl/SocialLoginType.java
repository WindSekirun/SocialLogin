package pyxis.uzuki.live.sociallogin.impl;

/**
 * SocialLogin
 * Class: SocialLoginType
 * Created by Pyxis on 2017-10-27.
 * <p>
 * Description:
 */

public class SocialLoginType {
    private SocialType type;
    private SocialConfig config;

    public SocialLoginType(SocialType type, SocialConfig config) {
        this.type = type;
        this.config = config;
    }

    public SocialType getType() {
        return type;
    }

    public void setType(SocialType type) {
        this.type = type;
    }

    public SocialConfig getConfig() {
        return config;
    }

    public void setConfig(SocialConfig config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "SocialLoginType{" +
                "type=" + type +
                ", config=" + config +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialLoginType that = (SocialLoginType) o;

        if (type != that.type) return false;
        return config != null ? config.equals(that.config) : that.config == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (config != null ? config.hashCode() : 0);
        return result;
    }
}
