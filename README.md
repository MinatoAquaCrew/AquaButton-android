AquaButton ⚓
======

[![GPLv3 license](https://img.shields.io/github/license/MinatoAquaCrew/AquaButton-android.svg)](https://github.com/MinatoAquaCrew/AquaButton-android/blob/master/LICENSE)
[![GitHub release](https://img.shields.io/github/release/MinatoAquaCrew/AquaButton-android.svg)](https://gitHub.com/MinatoAquaCrew/AquaButton-android/releases/)

**AquaButton** can play voices of [Minato Aqua（湊あくあ）](./AMT.md) on your Android phone/tablet.

Inspired by [zyzsdy/aqua-button (AquaButton Web)](https://aquaminato.moe/). We created an Android version for more features and cooler user interface on mobile.

Also experimented with a lot of features of Material Design like sounds, animations and components.

## Install

### Download pre-built package

Download from [GitHub Releases](https://github.com/MinatoAquaCrew/AquaButton-android/releases)

In the future, we will publish to Google Play and Coolapk.

### Build your own package

Build environment:

- Android Studio 4.1+
- Android SDK (Platform Q SDK, Build tools and etc.)
- Java 8+

Build step:

1. Use `git clone` clone repository to local
2. Open project root directory, connect your Android devices and run `./gradlew installDebug`

## Screenshot

[![Watch on YouTube](https://img.youtube.com/vi/BdhIOnJCVQI/hqdefault.jpg)](https://youtu.be/BdhIOnJCVQI)

## Contributing

### Voices

We used voice assets from repository of [zyzsdy/aqua-button (AquaButton Web)](https://github.com/zyzsdy/aqua-button).

You can read description in this repo about how to add or modify voice.

### Translations

Please help us translate into English and Japanese (or more languages!)

The language files for the main program are created in standard Android project structure. For example, Japanese translation is saved in [`app/src/main/res/values-ja/strings.xml`](https://github.com/MinatoAquaCrew/AquaButton-android/blob/master/app/src/main/res/values-ja/strings.xml).

The language files for voices are in [zyzsdy/aqua-button (AquaButton Web)](https://github.com/zyzsdy/aqua-button).

### Program

Feel free to send issues and pull requests to this repository.

## Licenses

This project and GitHub organization is a work of enthusiasts and is not related to the hololive official.

### Main program

```
GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007

Copyright (C) 2020 MinatoAquaCrew

This program comes with ABSOLUTELY NO WARRANTY.
This is free software, and you are welcome to redistribute it under certain conditions.
```

### Voices

According to the [Hololive secondary creation licence](https://www.hololive.tv/terms).

### Design (Icons, logos, sounds and etc.)

Read [README in /design](./design/README.md)
