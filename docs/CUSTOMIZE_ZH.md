定制你的 Fork 版本
------

你可以 Fork 这个项目，然后为你喜爱的 VTB 进行定制。

这里有一些你必须要做的改动：

## 应用 ID（Application id）和包名（Package name）

Android 使用应用 ID 来辨别不同的应用程序，如果两个应用包的 ID 相同，则无法在同一设备上同时安装。

首先，你需要修改在 [`app/build.gradle`](../app/build.gradle) 里的应用 ID：

```groovy
android {
    ...

    defaultConfig {
        // 用你新的应用 ID 来替换，例如 “aquacrew.matsuributton”
        applicationId "aquacrew.aquabutton"

        ...
    }
}
```

然后修改在 [`AndroidManifest.xml`](../app/src/main/AndroidManifest.xml) 里的包名（Package name）：

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest ...
    package="aquacrew.aquabutton">
    <!-- ↑ 替换 package 属性为和应用 ID 相同的值 -->

    ...

</manifest>
```

现在你可以试着编译应用模块来测试应用 ID 和包名是否成功更改。

通常来说，你会看到一些编译错误指出找不到 `aquacrew.aquabutton.R` 和 `aquacrew.aquabutton.BuildConfig` 类。

因为 `R` 和 `BuildConfig` 的包由 `AndroidManifest.xml` 里的包名决定生成。

我们推荐你添加缺少的 import 语句来修复，而不是重构所有的源码到新的包。这样对你今后从上游合并提交到你的 Fork 中会更加容易。

如果你仍然遇到编译错误，你可以发送 Issue 或者寻找是否已存在相同问题的 Issue。

## 实现你的 API

目前我们使用在 [zyzsdy/aqua-button (AquaButton Web)](https://github.com/zyzsdy/aqua-button) 里的语音资源，没有任何自建服务和 CDN 加速，并且只提供了湊あくあ的语音。

要提供你最喜爱的 VTB 语音数据，你需要实现 [`IAssetsApiProvider`](../app/src/main/java/aquacrew/aquabutton/api/provider/IAssetsApiProvider.kt)。（样例实现：[`AquaButtonApiProvider`](../app/src/main/java/aquacrew/aquabutton/api/provider/AquaButtonApiProvider.kt)）

然后编辑  [`AquaApp`](../app/src/main/java/aquacrew/aquabutton/AquaApp.kt) 里的 `onCreate` 方法：

```kotlin
override fun onCreate() {
    ...
    // 在应用创建时安装 API 提供类的实现
    AssetsApi.installProvider(MyVTuberApiProvider())

    ...
}
```

## 编辑文本资源

在 [`strings.xml`](../app/src/main/res/values/strings.xml) 编辑 `app_name` 内容为你的新应用名。

[`vtuber_strings.xml`](../app/src/main/res/values/vtuber_strings.xml) 这里还有一些 VTuber 的官方公开信息和版权信息。

不要忘了还有其它翻译版本在 `values-XX` 资源文件夹中需要编辑，或者删除掉未翻译的 `vtuber_strings.xml`。

## 编辑你的图案

用你的设计更换启动器图标和主图案。
