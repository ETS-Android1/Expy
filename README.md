# Expy

[![GitHub release][release-shield]][release-url]
[![MIT License][license-shield]][license-url]

<a><img src="https://i.imgur.com/HcEC3Fc.png"/></a>

**Expy allows you to keep track of the expiration dates of the products you're using to ensure the effective use of the products.** The main features of this Android application are recording the expiration date of foods or other products, getting periodic alert notifications, and accessing quick monitoring from a widget. Other features include one-step login using Google, storing and synchronizing your records across devices, support for two languages (Indonesian and English), and a random profile picture.

Expy was developed with the team as a capstone project for the Mobile Application Programming 2 course. The development process went through 4 stages, namely analysis, planning, implementation, and testing.

## Download
Check out the [release page](https://github.com/ariefzuhri/Expy/releases) and download the latest apk.

## Architecture and Tech-stack
- Native Android with Java
- [MVVM pattern](https://developer.android.com/jetpack/guide#recommended-app-arch)
- Android Architecture Components, specifically [Room](https://developer.android.com/topic/libraries/architecture/room), [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata), [Paging](https://developer.android.com/topic/libraries/architecture/paging), and [Material Components](https://material.io/develop/android)
- [Firebase Authentication](https://firebase.google.com/docs/auth), secure user authentication systems
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android), support for user authentication using Google account
- [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore), a NoSQL document database to store, sync, and query data
- [Firebase Analytics](https://firebase.google.com/docs/analytics), provides insight on app usage and user engagement
- [Glide](https://github.com/bumptech/glide), image loading and caching
- [Material View Pager Dots Indicator](https://github.com/tommybuonomo/dotsindicator), indicators for view pagers
- [Facebook Shimmer](https://github.com/facebook/shimmer-android), shimmering effect on loading screen
- [CircleImageView](https://github.com/hdodenhof/CircleImageView), a circular image view

## Configuration
Firstly, clone this repository and import it into Android Studio (`git clone https://github.com/ariefzuhri/Expy.git`).

### Setup Firebase Project
1. Open the [Firebase Console](https://console.firebase.google.com/) and click `Add project` (or use an existing Firebase sandbox project).
2. Follow the instructions there to create a new Firebase project.
3. Go to `Project Settings` and at the bottom, click on the Android icon to register your Android app.
4. Follow the instructions there again and make sure the package name matches your app ID in the Android Studio project. It is also required to include a `debug signing certificate SHA-1` to support Google Sign-In in Firebase Authentication.
6. Download the `google-services.json` file and move it into Android app module root directory (`./app`).

### Setup Firebase Authentication
1. Go to `Authentication` and click the `Get started` button to enable it.
2. Enable `Google` in the `Sign-in method` section.

### Setup Firebase Cloud Firestore
1. Go to `Firestore Database` and click the `Create database` button to enable it.
2. Select `Start in test mode` as the `Security rules`.
3. Select `nam5 (us-central)` as the `Database location`.
4. Click the `Enable` button.

### Setup Firebase Cloud Storage
1. Go to `Storage` and click the `Get started` button to enable it.
2. Select `Start in test mode` as the `Security rules`.
3. Select `nam5 (us-central)` as the `Database location`.
4. Click the `Done` button.

## 🤝 Support
Any contributions, issues, and feature requests are welcome.

Give a ⭐️ if you like this project.

## License
This project is licensed under the MIT License. See the [`LICENSE`](https://github.com/ariefzuhri/Expy/blob/master/LICENSE) file for details.

## Acknowledgments
- [Gagandeep Singh](https://www.sitepoint.com/author/gagandeepsingh/) on [Killer Way to Show a List of Items in Android Collection Widget](https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/)
- [Ravi Tamada](https://www.androidhive.info/author/admin/) on [Android Working with Bottom Sheet](https://www.androidhive.info/2017/12/android-working-with-bottom-sheet/)
- [Figma](https://www.figma.com), [Feather Icons](https://www.figma.com/community/plugin/744047966581015514/Feather-Icons), [Heroicons](https://www.figma.com/community/plugin/876119978690687541/Heroicons), [Humaaans for Figma](https://www.figma.com/community/plugin/739503328703046360/Humaaans-for-Figma), [Material Design Icons (Community)](https://www.figma.com/community/plugin/775671607185029020/Material-Design-Icons-(Community))
- [Freepik](https://www.freepik.com)
- [Shields.io](https://shields.io/)

## Developer team
- [Arief Zuhri](https://github.com/ariefzuhri)
- [Gabriela Anggerita Jasmin](https://github.com/anggerita)

[release-shield]: https://img.shields.io/github/v/release/ariefzuhri/Expy?include_prereleases&style=for-the-badge
[release-url]: https://github.com/ariefzuhri/Expy/releases
[license-shield]: https://img.shields.io/github/license/ariefzuhri/Expy?style=for-the-badge
[license-url]: https://github.com/ariefzuhri/Expy/blob/master/LICENSE
