# Documentation For GameChange Android Assignment

## Summary

#### [Use Gradle and its default project structure](#build-system)
#### [Put passwords and sensitive data in gradle.properties](#gradle-configuration)
#### [Use the Jackson library to parse JSON data](#libraries)
#### [Don't write your own HTTP client, use OkHttp libraries](#networklibs)
#### [Avoid Guava and use only a few libraries due to the *65k method limit*](#methodlimitation)
#### [Sail carefully when choosing between Activities and Fragments](#activities-and-fragments)
#### [Layout XMLs are code, organize them well](#resources)
#### [Use styles to avoid duplicate attributes in layout XMLs](#styles)
#### [Use multiple style files to avoid a single huge one](#splitstyles)
#### [Keep your colors.xml short and DRY, just define the palette](#colorsxml)
#### [Also keep dimens.xml DRY, define generic constants](#dimensxml)
#### [Do not make a deep hierarchy of ViewGroups](#deephierarchy)
#### [Avoid client-side processing for WebViews, and beware of leaks](#webviews)
#### [Use JUnit for unit tests, Espresso for connected (UI) tests, and AssertJ-Android for easier assertions in your Android tests](#test-frameworks)
#### [Always use ProGuard or DexGuard](#proguard-configuration)
#### [Use SharedPreferences for simple persistence, otherwise ContentProviders](#data-storage)
#### [Use Stetho to debug your application](#use-stetho)
#### [Use Leak Canary to find memory leaks](#use-leakcanary)
#### [Use continuous integration](#use-continuous-integration-1)

----------

### Android SDK

Place your [Android SDK](https://developer.android.com/sdk/installing/index.html?pkg=tools) somewhere in your home directory or some other application-independent location. Some distributions of IDEs include the SDK when installed, and may place it under the same directory as the IDE. This can be bad when you need to upgrade (or reinstall) the IDE, as you may lose your SDK installation, forcing a long and tedious redownload.

Also avoid putting the SDK in a system-level directory that might need root permissions, to avoid permissions issues.

### Build system

Your default option should be [Gradle](https://gradle.org) using the [Android Gradle plugin](https://developer.android.com/studio/build/index.html). 

It is important that your application's build process is defined by your Gradle files, rather than being reliant on IDE specific configurations. This allows for consistent builds between tools and better support for continuous integration systems.

### Project structure

Although Gradle offers a large degree of flexibility in your project structure, unless you have a compelling reason to do otherwise, you should accept its [default structure](https://developer.android.com/studio/build/index.html#sourcesets) as this simplify your build scripts. 

### Gradle configuration

**General structure.** Follow [Google's guide on Gradle for Android](https://developer.android.com/studio/build/index.html).

**minSdkVersion: 21** We recommend to have a look at the [Android version usage chart](https://developer.android.com/about/dashboards/index.html#Platform) before defining the minimum API required. Remember that the statistics given are global statistics and may differ when targeting a specific regional/demographic market. It is worth mentioning that some material design features are only available on Android 5.0 (API level 21) and above. And also, from API 21, the multidex support library is not needed anymore.

**Small tasks.** Instead of (shell, Python, Perl, etc) scripts, you can make tasks in Gradle. Just follow [Gradle's documentation](http://www.gradle.org/docs/current/userguide/userguide_single.html#N10CBF) for more details. Google also provides some helpful [Gradle recipes](https://developer.android.com/studio/build/gradle-tips.html), specific to Android.

**Passwords.** In your app's `build.gradle` you will need to define the `signingConfigs` for the release build. Here is what you should avoid:

_Don't do this_. This would appear in the version control system.

```groovy
signingConfigs {
    release {
        // DON'T DO THIS!!
        storeFile file("myapp.keystore")
        storePassword "password123"
        keyAlias "thekey"
        keyPassword "password789"
    }
}
```

Instead, make a `gradle.properties` file which should _not_ be added to the version control system:

```
KEYSTORE_PASSWORD=password123
KEY_PASSWORD=password789
```

That file is automatically imported by Gradle, so you can use it in `build.gradle` as such:

```groovy
signingConfigs {
    release {
        try {
            storeFile file("myapp.keystore")
            storePassword KEYSTORE_PASSWORD
            keyAlias "thekey"
            keyPassword KEY_PASSWORD
        }
        catch (ex) {
            throw new InvalidUserDataException("You should define KEYSTORE_PASSWORD and KEY_PASSWORD in gradle.properties.")
        }
    }
}
```

**Prefer Maven dependency resolution to importing jar files.** If you explicitly include jar files in your project, they will be a specific frozen version, such as `2.1.1`. Downloading jars and handling updates is cumbersome and is a problem that Maven already solves properly. Where possible, you should attempt to use Maven to resolve your dependencies, for example:

```groovy
dependencies {
    implementation 'com.squareup.okhttp:okhttp3:3.8.0'
}
```    

**Avoid Maven dynamic dependency resolution**
Avoid the use of dynamic dependency versions, such as `2.1.+` as this may result in different and unstable builds or subtle, untracked differences in behavior between builds. The use of static versions such as `2.1.1` helps create a more stable, predictable and repeatable development environment.

**Use different package name for non-release builds**
Use `applicationIdSuffix` for *debug* [build type](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Build-Types) to be able to install both *debug* and *release* apk on the same device (do this also for custom build types, if you need any). This will be especially valuable after your app has been published.

```groovy
android {
    buildTypes {
        debug {
            applicationIdSuffix '.debug'
            versionNameSuffix '-DEBUG'
        }

        release {
            // ...
        }
    }
}
```

Use different icons to distinguish the builds installed on a device—for example with different colors or an overlaid  "debug" label. Gradle makes this very easy: with default project structure, simply put *debug* icon in `app/src/debug/res` and *release* icon in `app/src/release/res`. You could also [change app name](http://stackoverflow.com/questions/24785270/how-to-change-app-name-per-gradle-build-type) per build type, as well as  `versionName` (as in the above example).

**Share debug app keystore file**
Sharing the debug APK keystore file via the app repository saves time when testing on shared devices and avoids the uninstalling/reinstalling of the app. It also simplifies the processing of working with some Android SDKs, such as Facebook, which require the registration of a single key store hash. Unlike the release key file, the debug key file can safely be added to your repository.

**Share code style formatting defintions**
Sharing the code style and formatting definitions via the app repository helps ensure a visually consistent code base and makes code comprehension and reviews easier.

### Android Studio as your main IDE

The recommended IDE for Android development is [Android Studio](https://developer.android.com/sdk/installing/studio.html) because it is developed and constantly updated by Google, has good support for Gradle, contains a range of useful monitoring and analysis tools and is fully tailored for Android development.

Avoid adding Android Studio's specific configuration files, such as `.iml` files to the version control system as these often contain configurations specific of your local machine, which won't work for your colleagues.

### Libraries

**[Jackson](http://wiki.fasterxml.com/JacksonHome)** is a Java library for JSON serialization and deserialization, it has a wide-scoped and versatile API, supporting various ways of processing JSON: streaming, in-memory tree model, and traditional JSON-POJO data binding. 

[Gson](https://code.google.com/p/google-gson/) is another popular choice and being a smaller library than Jackson, you might prefer it to avoid 65k methods limitation. Also, if you are using  

[Moshi](https://github.com/square/moshi), another of [Square's](https://github.com/square) open source libraries, builds upon learnings from the development of Gson and also integrates well with Kotlin.

<a name="networklibs"></a>
**Networking, caching, and images.** There are a couple of battle-proven solutions for performing requests to backend servers, which you should use rather than implementing your own client. We recommend basing your stack around [OkHttp](http://square.github.io/okhttp/) for efficient HTTP requests and using [Retrofit](http://square.github.io/retrofit/) to provide a typesafe layer. If you choose Retrofit, consider [Picasso](http://square.github.io/picasso/) for loading and caching images.

Retrofit, Picasso and OkHttp are created by the same company, so they complement each other nicely and compatability issues are uncommon.

**[Retrolambda](https://github.com/evant/gradle-retrolambda)** is a Java library for using Lambda expression syntax in Android and other pre-JDK8 platforms. It helps keep your code tight and readable especially if you use a functional style, such as in RxJava.

Android Studio offers code assist support for Java 8 lambdas. If you are new to lambdas, just use the following to get started:

- Any interface with just one method is "lambda friendly" and can be folded into the more tight syntax
- If in doubt about parameters and such, write a normal anonymous inner class and then let Android Studio fold it into a lambda for you.

Note that from Android Studio 3.0, [Retrolambda is no longer required](https://developer.android.com/studio/preview/features/java8-support.html).

<a name="methodlimitation"></a>
**Beware of the dex method limitation, and avoid using many libraries.** Android apps, when packaged as a dex file, have a hard limitation of 65536 referenced methods [[1]](https://medium.com/@rotxed/dex-skys-the-limit-no-65k-methods-is-28e6cb40cf71) [[2]](http://blog.persistent.info/2014/05/per-package-method-counts-for-androids.html) [[3]](http://jakewharton.com/play-services-is-a-monolith/). You will see a fatal error on compilation if you pass the limit. For that reason, use a minimal amount of libraries, and use the [dex-method-counts](https://github.com/mihaip/dex-method-counts) tool to determine which set of libraries can be used in order to stay under the limit. Especially avoid using the Guava library, since it contains over 13k methods.

### Activities and Fragments

### List Of Comment Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:background="@color/background_layout"
    android:layout_height="wrap_content">

    <androidx.cardview.widget.CardView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/five_dp"
        app:cardElevation="@dimen/five_dp"
        app:cardCornerRadius="@dimen/five_dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

    <LinearLayout
       android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <com.io.stay_active.custom.BoldTextView
            android:id="@+id/tvIssueTitle"
           android:layout_marginLeft="@dimen/eight_dp"
            android:layout_marginTop="@dimen/ten_dp"
            android:textSize="@dimen/fourteen_sp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/title" />
        <com.io.stay_active.custom.NormalTextView
            android:id="@+id/tvIsssuedesc"
            android:layout_marginBottom="@dimen/ten_dp"
            android:layout_marginLeft="@dimen/eight_dp"
            android:layout_marginTop="@dimen/ten_dp"
            android:textSize="@dimen/fourteen_sp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/title" />
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <com.io.stay_active.custom.BoldTextView
                android:layout_alignParentRight="true"
                android:id="@+id/tvDate"
                android:layout_marginLeft="@dimen/twenty_four_dp"
                android:layout_marginBottom="@dimen/ten_dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/time" />
        </RelativeLayout>
    </LinearLayout>
    </androidx.cardview.widget.CardView>
</androidx.constraintlayout.widget.ConstraintLayout>
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".activity.GithubIssueActivity">

    <include
        android:id="@+id/include"
        layout="@layout/toolbar"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

    </include>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/issueRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="8dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/include" />
</androidx.constraintlayout.widget.ConstraintLayout>

As a rule of thumb, attributes `android:layout_****` should be defined in the layout XML, while other attributes `android:****` should stay in a style XML. This rule has exceptions, but in general works fine. The idea is to keep only layout (positioning, margin, sizing) and content attributes in the layout files, while keeping all appearance details (colors, padding, font) in styles files.

The exceptions are:

- `android:id` should obviously be in the layout files
- `android:orientation` for a `LinearLayout` normally makes more sense in layout files
- `android:text` should be in layout files because it defines content
- Sometimes it will make sense to make a generic style defining `android:layout_width` and `android:layout_height` but by default these should appear in the layout files

<a name="styles"></a>
**Use styles.** Almost every project needs to properly use styles, because it is very common to have a repeated appearance for a view. At least you should have a common style for most text content in the application, for example:

```xml
   <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
```

Applied to TextViews:

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/price"
    style="@style/ContentText"
    />
```

```xml
<resources>
      <color name="colorPrimary">#008577</color>
        <color name="colorPrimaryDark">#00574B</color>
        <color name="colorAccent">#D81B60</color>
        <color name="background_layout">#cdf1f4f6</color>
        <color name="text_color">#6e777f</color>
        <color name="text_color_dark_gray">#495057</color>
        <color name="background_on_focus">#19fdbb1f</color>
        <color name="yellow">#fdbb1f</color>
        <color name="light_gray">#D3D3D3</color>
        <color name="dark_gray_text_color">#495057</color>
        <color name="white">#ffffff</color>
        <color name="streoke_dialog">#cdf1f4f6</color>
        <color name="blue">#3e50b5</color>
</resources>    
```

Instead, do this:


This approach offers improved color refactoring and more stable style definitions when multiple related styles share similar color and usage properties. However, it comes at the cost of maintaining another set of color mappings. 

<a name="dimensxml"></a>
**Treat dimens.xml like colors.xml.** You should also define a "palette" of typical spacing and font sizes, for basically the same purposes as for colors. A good example of a dimens file:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="sixteen_dp">16dp</dimen>
    <dimen name="fourteen_sp">14sp</dimen>
    <dimen name="twenty_sp">20sp</dimen>
    <dimen name="ten_dp">10dp</dimen>
    <dimen name="eight_dp">8dp</dimen>
    <dimen name="twenty_four_dp">24dp</dimen>
    <dimen name="thirty_dp">30dp</dimen>
    <dimen name="onefiftydp">150dp</dimen>
    <dimen name="twelve_dp">12dp</dimen>
    <dimen name="sixteen_sp">16sp</dimen>
    <dimen name="two_Fifty_dp">250dp</dimen>
    <dimen name="two_hundred_dp">200dp</dimen>
    <dimen name="twenty_two_dp">22dp</dimen>
    <dimen name="five_dp">5dp</dimen>
    <dimen name="eighteen_sp">18sp</dimen>
    <dimen name="twenty_five_dp">25dp</dimen>
    <dimen name="fifty_sixdp">56dp</dimen>
</resources>
```

**strings.xml**

Name your strings with keys that resemble namespaces, and don't be afraid of repeating a value for two or more keys. Languages are complex, so namespaces are necessary to bring context and break ambiguity.

**Bad**
```xml
<string name="network_error">Network error</string>
<string name="call_failed">Call failed</string>
<string name="map_failed">Map loading failed</string>
```

**Good**
```xml
<string name="error_message_network">Network error</string>
<string name="error_message_call">Call failed</string>
<string name="error_message_map">Map loading failed</string>
```

Don't write string values in all uppercase. Stick to normal text conventions (e.g., capitalize first character). If you need to display the string in all caps, then do that using for instance the attribute [`textAllCaps`](http://developer.android.com/reference/android/widget/TextView.html#attr_android:textAllCaps) on a TextView.

**Bad**
```xml
<string name="error_message_call">CALL FAILED</string>
```

**Good**
```xml
<string name="error_message_call">Call failed</string>
```

<a name="deephierarchy"></a>
**Avoid a deep hierarchy of views.** Sometimes you might be tempted to just add yet another LinearLayout, to be able to accomplish an arrangement of views. This kind of situation may occur:

```

Even if you don't witness this explicitly in a layout file, it might end up happening if you are inflating (in Java) views into other views.

A couple of problems may occur. You might experience performance problems, because there is a complex UI tree that the processor needs to handle. Another more serious issue is a possibility of [StackOverflowError](http://stackoverflow.com/questions/2762924/java-lang-stackoverflow-error-suspected-too-many-views).


### Test Frameworks

**Use [JUnit](https://developer.android.com/training/testing/unit-testing/local-unit-tests.html) for unit testing** Plain, Android dependency-free unit testing on the JVM is best done using [Junit](https://junit.org). 

**Avoid [Robolectric](http://robolectric.org/)** Prior to the improved support for JUnit in the Android build system, Robolectric was promoted as a test framework seeking to provide tests "disconnected from device" for the sake of development speed. However, testing under Robolectric is inaccurate and incomplete as it works by providing mock implementations of the Android platform, which provides no guarantees of correctness. Instead, use a combination of JVM based unit tests and dedicated on-device integration tests.

**[Espresso](https://developer.android.com/training/testing/ui-testing/espresso-testing.html) makes writing UI tests easy.**

**[AssertJ-Android](http://square.github.io/assertj-android/) an AssertJ extension library making assertions easy in Android tests**  Assert-J comes modules easier for you to test Android specific components, such as the Android Support, Google Play Services and Appcompat libraries.

A test assertion will look like:

```java
// Example assertion using AssertJ-Android
assertThat(layout).isVisible()
    .isVertical()
    .hasChildCount(5);
```

**strings.xml**
### Emulators

The performance of the Android SDK emulator, particularly the x86 variant, has improvement markedly in recent years and is now adequate for most day-to-day development scenarios. However, you should not discount the value of ensuring your application behaves correctly on real devices. Of course, testing on all possible devices is not practical, so rather focus your efforts on devices with a large market share and those most relevant to your app.

### Proguard configuration

[ProGuard](http://proguard.sourceforge.net/) is normally used on Android projects to shrink and obfuscate the packaged code.

Whether you are using ProGuard or not depends on your project configuration. Usually you would configure Gradle to use ProGuard when building a release APK.

```groovy
buildTypes {
    debug {
        minifyEnabled false
    }
    release {
        signingConfig signingConfigs.release
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }
}
```

In order to determine which code has to be preserved and which code can be discarded or obfuscated, you have to specify one or more entry points to your code. These entry points are typically classes with main methods, applets, midlets, activities, etc.
Android framework uses a default configuration which can be found from `SDK_HOME/tools/proguard/proguard-android.txt`. Using the above configuration, custom project-specific ProGuard rules, as defined in `my-project/app/proguard-rules.pro`, will be appended to the default configuration.

A common problem related to ProGuard is to see the application crashing on startup with `ClassNotFoundException` or `NoSuchFieldException` or similar, even though the build command (i.e. `assembleRelease`) succeeded without warnings.
This means one out of two things:

1. ProGuard has removed the class, enum, method, field or annotation, considering it's not required.
2. ProGuard has obfuscated (renamed) the class, enum or field name, but it's being used indirectly by its original name, i.e. through Java reflection.

Check `app/build/outputs/proguard/release/usage.txt` to see if the object in question has been removed.
Check `app/build/outputs/proguard/release/mapping.txt` to see if the object in question has been obfuscated.

In order to prevent ProGuard from *stripping away* needed classes or class members, add a `keep` options to your ProGuard config:
```
-keep class com.futurice.project.MyClass { *; }
```

To prevent ProGuard from *obfuscating* classes or class members, add a `keepnames`:
```
-keepnames class com.futurice.project.MyClass { *; }
```

Read more at [Proguard](https://www.guardsquare.com/en/proguard/manual/examples) for examples.

**Early in your project, make and test release build** to check whether ProGuard rules are correctly retaining your dependencies. Also whenever you include new libraries or update their dependencies, make a release build and test the APK on a device. Don't wait until your app is finally version "1.0" to make a release build, you might get several unpleasant surprises and a short time to fix them.

**Tip.** Save the `mapping.txt` file for every release that you publish to your users. By retaining a copy of the `mapping.txt` file for each release build, you ensure that you can debug a problem if a user encounters a bug and submits an obfuscated stack trace.

**DexGuard**. If you need hard-core tools for optimizing, and specially obfuscating release code, consider [DexGuard](http://www.saikoa.com/dexguard), a commercial software made by the same team that built ProGuard. It can also easily split Dex files to solve the 65k methods limitation.

### Data storage


#### SharedPreferences

If you only need to persist simple values and your application runs in a single process SharedPreferences is probably enough for you. It is a good default option. 

There are some situations where SharedPreferences are not suitable:

* *Performance*: Your data is complex or there is a lot of it
* *Multiple processes accessing the data*: You have widgets or remote services that run in their own processes and require synchronized data
* *Relational data* Distinct parts of your data are relational and you want to enforce that those relationships are maintained.

You can also store more complex objects by serializing them to JSON to store them and deserializing them when retrieving. You should consider the tradeoffs when doing this as it may not be particularly performant, nor maintainable.

#### ContentProviders

In case SharedPreferences are not enough, you should use the platform standard ContentProviders, which are fast and process safe.

The single problem with ContentProviders is the amount of boilerplate code that is needed to set them up, as well as low quality tutorials. It is possible, however, to generate the ContentProvider by using a library such as [Schematic](https://github.com/SimonVT/schematic), which significantly reduces the effort.

You still need to write some parsing code yourself to read the data objects from the Sqlite columns and vice versa. It is possible to serialize the data objects, for instance with Gson, and only persist the resulting string. In this way you lose in performance but on the other hand you do not need to declare a column for all the fields of the data class.

#### Using an ORM

We generally do not recommend using an Object-Relation Mapping library unless you have unusually complex data and you have a dire need. They tend to be complex and require time to learn. If you decide to go with an ORM you should pay attention to whether or not it is _process safe_ if your application requires it, as many of the existing ORM solutions surprisingly are not.

### Use Stetho 

[Stetho](http://facebook.github.io/stetho/) is a debug bridge for Android applications from Facebook that integrates with the Chrome desktop browser's Developer Tools. With Stetho you can easily inspect your application, most notably the network traffic. It also allows you to easily inspect and edit SQLite databases and the shared preferences in your app. You should, however, make sure that Stetho is only enabled in the debug build and not in the release build variant. 

Another alternative is [Chuck](https://github.com/jgilfelt/chuck) which, although offering slightly more simplified functionality, is still useful for testers as the logs are displayed on the device, rather than in the more complicated connected Chrome browser setup that Stetho requires.

### Use LeakCanary

[LeakCanary](https://github.com/square/leakcanary) is a library that makes runtime detection and identification of memory leaks a more routine part of your application development process. See the library [wiki](https://github.com/square/leakcanary/wiki) for details on configuration and usage. Just remember to configure only the "no-op" dependency in your release build!

### Use continuous integration

Continuous integration systems let you automatically build and test your project every time you push updates to version control. Continuous integration also runs static code analysis tools, generates the APK files and distributes them.
[Lint](https://developer.android.com/studio/write/lint.html) and [Checkstyle](http://checkstyle.sourceforge.net/) are tools that ensure the code quality while [Findbugs](http://findbugs.sourceforge.net/) looks for bugs in the code.
   
There is a wide variety of continuous integration software which provide different features. Pricing plans might be for free if your project is open-sourced.
[Jenkins](https://jenkins.io/) is a good option if you have a local server at your disposal, on the other hand Zenkins is also a recommended choice if you plan to use a cloud-based continuous integration service.

#