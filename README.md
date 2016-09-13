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
#### Init Qiscus
Init Qiscus at your application class
```java
public class SampleApps extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Qiscus.init(this, "yourQiscusAppId");
    }
}
```


#### Login to Qiscus engine
Before user can start chatting each other, they must login to qiscus engine.
```java
Qiscus.with("user@email.com", "password")
      .login(new Qiscus.LoginListener() {
          @Override
          public void onSuccess(QiscusAccount qiscusAccount) {
              Log.i(TAG, "Login with account: " + qiscusAccount);
          }
          @Override
          public void onError(Throwable throwable) {
              throwable.printStackTrace();
              showError(throwable.getMessage());
          }
      });
```


### Start the chatting
```java
Qiscus.buildChatWith("jhon.doe@gmail.com")
      .withTitle("Jhon Doe")
      .build(this, new Qiscus.ChatActivityBuilderListener() {
          @Override
          public void onSuccess(Intent intent) {
              startActivity(intent);
          }
          @Override
          public void onError(Throwable throwable) {
              throwable.printStackTrace();
              showError(throwable.getMessage());
          }
      });
```


### Customize the chat UI
Dont like the default template? You can customize it :

```java
Qiscus.getChatConfig()
      .setStatusBarColor(R.color.blue)
      .setAppBarColor(R.color.red)
      .setTitleColor(R.color.white)
      .setLeftBubbleColor(R.color.green)
      .setRightBubbleColor(R.color.yellow)
      .setRightBubbleTextColor(R.color.white)
      .setRightBubbleTimeColor(R.color.grey)
      .setTimeFormat(date -> new SimpleDateFormat("HH:mm").format(date));
```



