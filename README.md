# Android SDK for PersianCal
[![Build Status](https://travis-ci.org/persiancal/android-sdk.svg?branch=master)](https://travis-ci.org/persiancal/android-sdk)
[![](https://jitpack.io/v/persiancal/android-sdk.svg)](https://jitpack.io/#persiancal/android-sdk)

# Setup
## 1. Provide the gradle dependency
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Add the dependency:
```gradle
dependencies {
    //required
    implementation "com.github.persiancal.android-sdk:core:${latestRelease}"
    
    //optional
    implementation "com.github.persiancal.android-sdk:sdk-local:${latestRelease}"
    implementation "com.github.persiancal.android-sdk:sdk-remote:${latestRelease}"
}
```

## 2. Add codes

add INTERNET permission to android manifest
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
implement init function of RemoteClandarEvents in application class
```kotlin
override fun onCreate() {
        super.onCreate()
        ...
        RemoteCalendarEvents
            .addCalendar(CalendarType.JALALI)
            .addCalendar(CalendarType.HIJRI)
            .addCalendar(CalendarType.GREGORIAN)
            .init(this)
        ...
}
```
You can add each type of calenders that want

there is three function for getting calendar events
```kotlin
- getJalaliEvents(dayOfMonth: Int, month: Int): MutableList<RemoteJalaliEventsDb>?
- getHijriEvents(dayOfMonth: Int, month: Int): MutableList<RemoteHijriEventsDb>?
- getGregorianEvents(dayOfMonth: Int, month: Int): MutableList<RemoteGregorianEventsDb>?
```
After init RemoteCalendarEvents you can access instance of it everywhere:
```kotlin
RemoteCalendarEvents.getInstance()
```
for example we want `Jalali` events of `month=2` & `dayOfMonth=1`
```kotlin
val jalaliEvents= RemoteCalendarEvents.getInstance().getJalaliEvents(1,2)
```

**Requirements**
- Android Studio 3.5.1
- JDK 8
- Android SDK 29
- Supports API Level +21
- Material Components 1.1.0-beta01

**Libraries & Dependencies in the Demo**
- [Support libraries]: appcompat / recyclerview / constraintlayout
- [Material Design 2]: MaterialCardView / MaterialButton 
- [FastAdapter]: The bullet proof, fast and easy to use adapter library, which minimizes developing time to a fraction
- [PrimeDatePicker]: is a tool which provides picking a single day as well as a range of days.

**Libraries & Dependencies in the SDK**
- Square [Retrofit] / [Okhttp] / [Logging-Interceptor]
- [RxAndroid] Reactive Extensions for Android

[Support libraries]: https://developer.android.com/jetpack/androidx/
[Material Design 2]: https://material.io/develop/android/
[FastAdapter]: https://github.com/mikepenz/FastAdapter
[PrimeDatePicker]: https://github.com/aminography/PrimeDatePicker
[Retrofit]: https://github.com/square/retrofit
[Okhttp]: https://github.com/square/okhttp
[Logging-Interceptor]: https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
[RxAndroid]: https://github.com/ReactiveX/RxAndroid
