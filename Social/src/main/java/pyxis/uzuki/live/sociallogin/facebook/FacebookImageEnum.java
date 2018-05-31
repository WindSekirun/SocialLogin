package pyxis.uzuki.live.sociallogin.facebook;

/**
 * SocialLogin
 * Class: FacebookImageEnum
 * Created by Pyxis on 5/31/18.
 * <p>
 * Description:
 */

public enum FacebookImageEnum {
    Small("picture.type(small)"), Normal("picture.type(normal)"), Album("picture.type(album)"),
    Large("picture.type(large)"), Square("picture.type(square)");

    private String fieldName;

    FacebookImageEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
