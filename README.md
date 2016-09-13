# Qiscus SDK

Qiscus SDK is a lightweight and powerful android chat library. Qiscus SDK will allow you to easyly integrating Qiscus engine with your apps to make cool chatting application.

# Install Qiscus SDK
Add to your project build.gradle
```groovy
allprojects {
    repositories {
        .....
        maven { url "https://jitpack.io" }
        maven { url 'http://clojars.org/repo' }
    }
}
```

Then add to your app module build.gradle
```groovy
dependencies {
    compile 'com.github.qiscus:qiscus-sdk-android:1.3'
}
```


# Initiate Qiscus SDK
## SampleApp.Java
### Init Qiscus
Init Qiscus at your application class
```java
public class SampleApps extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Qiscus.init(this, "DRAGONFLY");
    }
}
```


## MainActivity.java
### Login to Qiscus engine
Before user can start chatting each other, they must login to qiscus engine.
```java
        Qiscus.with("e3@qiscus.com", "password")
                .login(new Qiscus.LoginListener() {
                    @Override
                    public void onSuccess(QiscusAccount qiscusAccount) {


                    }
                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
```


### Start the chatting
```java
        Qiscus.buildChatWith("e2@qiscus.com")
                .withTitle("Evan")
                .build(MainActivity.this, new Qiscus.ChatActivityBuilderListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivity(intent);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
```


### Customize the chat UI
Dont like the default template? You can customize it :

```java
        Qiscus.getChatConfig()
                .setStatusBarColor(android.R.color.holo_green_dark)
                .setAppBarColor(android.R.color.holo_green_dark)
                .setTitleColor(android.R.color.white)
                .setLeftBubbleColor(android.R.color.holo_green_dark)
                .setRightBubbleColor(android.R.color.holo_blue_dark)
                .setRightBubbleTextColor(android.R.color.white)
                .setRightBubbleTimeColor(android.R.color.white);
```


## Screen Shoots

![alt tag](https://qiscuss3.s3.amazonaws.com/e@qiscus.com/23207/5b6b6606c7cf9950cb194633972c003a/Screenshot_20160913-163838.png)

![alt tag](https://qiscuss3.s3.amazonaws.com/e@qiscus.com/23207/5b6b6606c7cf9950cb194633972c003a/Screenshot_20160913-163838.png)


