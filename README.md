## SocialLogin [![](https://jitpack.io/v/WindSekirun/SocialLogin.svg)](https://jitpack.io/#WindSekirun/SocialLogin)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

It provides integrated social login feature which have facebook, naver, kakao, line, twitter, google.

If you are korean, please see this [README](https://github.com/WindSekirun/SocialLogin/blob/master/README_KO.md)

## Usages

*rootProject/build.gradle*
```	
allprojects {
    repositories {
    	    maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
	    maven { url 'https://jitpack.io' }
    }
}
```

*app/build.gradle*
```
dependencies {
    implementation 'com.github.WindSekirun:SocialLogin:1.1.5'
}
```

## Available Feature
|Service|logout|Return Data|Config|
|---|---|---|---|
|Facebook|O|ID, NAME, EMAIL, PROFILE_PICTURE, GENDER, FIRST_NAME|setRequireEmail, setRequireWritePermission, setApplicationId, setRequireFriends, setBehaviorOnCancel, setPictureSize|
|Google|O|ID, NAME, EMAIL, ACCESS_TOKEN|setRequireEmail|
|Kakao|O|ID, NICKNAME, EMAIL, PROFILE_PICTURE, EMAIL_VERIFIED, THUMBNAIL_IMAGE|setRequireEmail, setRequireNickname, setSecureResource, setRequireImage|
|Line|X|ID, NAME, ACCESS_TOKEN|setChannelId|
|Naver|O|ID, NAME, EMAIL, NICKNAME, GENDER, PROFILE_PICTURE, AGE, BIRTHDAY|setAuthClientId, setAuthClientSecret, setClientName|
|Twitter|X|ID, NAME|setConsumerKey, setConsumerSecret|

## Guide

### Common
It can be copy-paste cause each serivce has same constructure.

#### Declare xxxLogin variables to use
```Java
private KakaoLogin kakaoModule;
```

#### generate new instance of xxxLogin
```Java
kakaoModule = new KakaoLogin(this, new OnResponseListener() {
    @Override
    public void onResult(SocialType socialType, ResultType resultType, Map<UserInfoType, String> map) {

    }
});
```

#### Login
```Java
kakaoModule.onLogin();
```

#### execute when onDestroy
```Java
kakaoModule.onDestroy();
```

#### execute when onActivityResult
```Java
kakaoModule.onActivityResult(requestCode, resultCode, data);
```

#### Description of enums
```Java
public enum SocialType { // type of social providers
    KAKAO, GOOGLE, FACEBOOK, LINE, NAVER, TWITTER;
}

public enum ResultType { 
    SUCCESS, FAILURE, CANCEL;
}

public enum UserInfoType { // field of information of user.
    ID, NAME, ACCESS_TOKEN, EMAIL, NICKNAME, PROFILE_PICTRUE, GENDER ...
}
```

### Kakao Login

### Dependencies

```
repositories {
    maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
}
```

```
  implementation 'com.kakao.sdk:usermgmt:1.11.1'
```

#### Add api key to AndroidManifest.xml
```XML
 <meta-data
            android:name="com.kakao.sdk.AppKey"
            android:value="<YOUR-API-KEY>"/>
```

#### initialize into Application 

```Java
SocialLogin.init(this);
KakaoConfig kakaoConfig = new KakaoConfig.Builder()
                .setRequireEmail()
                .setRequireNickname()
                .build();
		
SocialLogin.addType(SocialType.KAKAO, kakaoConfig);
```

#### using in activity
```Java
private KakaoLogin kakaoModule;
```


### Facebook login

#### add dependencies
```
   implementation 'com.facebook.android:facebook-android-sdk:4.23.0'
```

#### add activity into AndroidManifest.xml
```Java
<activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" />
	    
<meta-data android:name="com.facebook.sdk.ApplicationId"
            android:value=""<YOUR-API-KEY>"/>
```

#### initialize into Application 
```Java
SocialLogin.init(this);
FacebookConfig facebookConfig = new FacebookConfig.Builder()
                .setApplicationId("<YOUR-API-KEY>")
                .setRequireEmail()
                .build();
		

SocialLogin.addType(SocialType.FACEBOOK, facebookConfig);
```

#### using in activity
```Java
private FacebookLogin facebookModule;
```

### Naver Login

### add dependencies

```
implementation fileTree(include: ['*.jar', '*.aar'], dir: 'libs')
```

#### initialize into Application 
```Java
SocialLogin.init(this);
NaverConfig naverConfig = new NaverConfig.Builder()
                .setAuthClientId("<YOUR-API-KEY>")
                .setAuthClientSecret("<YOUR-API-KEY>")
                .setClientName(getString(R.string.app_name))
                .build();
                

SocialLogin.addType(SocialType.NAVER, naverConfig);
```

#### using in activity
```Java
private NaverLogin naverModule;
```

### Line Login

### add dependencies

```
implementation fileTree(include: ['*.jar', '*.aar'], dir: 'libs')
```

#### initialize into Application 
```Java
SocialLogin.init(this);
LineConfig lineConfig = new LineConfig.Builder()
                .setChannelId("<YOUR-API-KEY>")
                .build();


SocialLogin.addType(SocialType.LINE, lineConfig);
```

#### using in activity
```Java
private LineLogin lineModule;
```

### Twitter Login

#### add dependencies
```
    implementation 'com.twitter.sdk.android:twitter:3.1.0'
```

#### initialize into Application 
```Java
SocialLogin.init(this);
TwitterConfig twitterConfig = new TwitterConfig.Builder()
                .setConsumerKey("<YOUR-API-KEY>")
                .setConsumerSecret("<YOUR-API-KEY>")
                .build();


SocialLogin.addType(SocialType.TWITTER, twitterConfig);
```

#### using in activity
```Java
private TwitterLogin twitterModule;
```

### Google Login

#### Pre TODO
put your google-services.json in app module which can get at [Google Sign-in for Android](https://developers.google.com/identity/sign-in/android/start) 

#### add dependencies
```
    implementation 'com.google.android.gms:play-services-auth:10.2.6'
```

#### initialize into Application 
```Java
SocialLogin.init(this);
GoogleConfig googleConfig = new GoogleConfig.Builder()
                .setRequireEmail()
                .build();


SocialLogin.addType(SocialType.GOOGLE, googleConfig);
```

#### using in activity
```Java
private GoogleLogin googleModule;
```

## License
```
Copyright 2017 WindSekirun (DongGil, Seo)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
