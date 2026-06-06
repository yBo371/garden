**简体中文** · [English](./README_EN.md)

# Garden App

Garden App 是一个基于 Kotlin、Jetpack Compose 和 MVVM 架构实现的 Android 花园经营 Demo。应用围绕花田种植、市场购买、仓库库存、鲜花图鉴和个人成就展开，当前数据主要由内存状态和静态仓库数据驱动。

## 功能概览

- 花园页：展示 6 块花田，支持选择工具并对花田执行浇水、除虫、加速和收取操作。
- 种植流程：点击空花田后可从仓库选择种子播种，作物进入生长倒计时，并根据状态提示浇水或收获。
- 市场页：按分类浏览种子和园艺工具，使用金币购买未锁定商品，购买后更新仓库库存。
- 我的页：展示用户资料、鲜花图鉴、仓库物品和成就任务。
- 共享状态：金币、工具选择、花田状态、仓库库存和消息提示集中在 `GameStore` 中维护。

## 技术栈

- Kotlin
- Android Gradle Plugin 8.13.0
- Gradle Wrapper 8.13
- Jetpack Compose + Material 3
- AndroidX Lifecycle / ViewModel / Navigation Compose
- Coil Compose，用于加载远程图片
- Java 21 / Kotlin JVM target 21

## 项目结构

```text
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/gardenapp/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── GameStore.kt
│       │   │   └── GardenRepository.kt
│       │   ├── model/
│       │   │   └── Models.kt
│       │   ├── ui/
│       │   │   ├── GardenApp.kt
│       │   │   ├── components/
│       │   │   ├── navigation/
│       │   │   ├── screens/
│       │   │   └── theme/
│       │   └── viewmodel/
│       └── res/
├── design.html
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew.bat
```

说明：

- `design.html` 是视觉参考原型。
- `app/build/`、`.gradle/`、`.idea/` 等目录属于构建或 IDE 产物，不应作为业务源码维护。
- 当前仓库没有提交专用的 `app/src/test/` 或 `app/src/androidTest/` 测试目录。

## 环境要求

- Android Studio 或可用的 Android SDK
- JDK 21
- Windows 环境可直接使用仓库中的 `gradlew.bat`
- 网络可访问配置中的 Maven 镜像和图片 CDN；应用运行时需要 `INTERNET` 权限加载远程图片

Gradle 和依赖仓库已配置镜像：

- Gradle Wrapper 使用腾讯云 Gradle 镜像
- Maven 仓库优先使用阿里云镜像，并保留 `google()`、`mavenCentral()` 作为后备

## 构建与运行

在仓库根目录执行：

```powershell
.\gradlew.bat assembleDebug
```

安装到已连接的设备或模拟器：

```powershell
.\gradlew.bat installDebug
```

运行本地单元测试：

```powershell
.\gradlew.bat testDebugUnitTest
```

运行 Android 仪器测试：

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

清理构建产物：

```powershell
.\gradlew.bat clean
```

如果使用 Android Studio，建议先执行 `Sync Project with Gradle Files`，再构建或运行。

## 主要代码说明

- `MainActivity.kt`：应用入口，启用 edge-to-edge 并挂载 Compose 根组件。
- `ui/GardenApp.kt`：应用主框架，包含底部导航和三个页面入口。
- `ui/screens/GardenScreen.kt`：花园页，处理花田展示、工具栏和种子选择弹窗。
- `ui/screens/MarketScreen.kt`：市场页，处理商品分类、金币展示和购买动作。
- `ui/screens/ProfileScreen.kt`：个人页，展示资料、图鉴、仓库和成就。
- `data/GameStore.kt`：全局游戏状态，包含播种、浇水、除虫、加速、收获、购买和倒计时刷新逻辑。
- `data/GardenRepository.kt`：静态初始数据，包括花田、工具、市场商品、图鉴和个人资料。
- `viewmodel/`：将 `GameStore` 和静态数据转换为各页面可消费的 UI 状态。

## 当前限制

- 游戏状态仅保存在内存中，应用重启后会恢复默认状态。
- 市场、图鉴、个人资料和图片 URL 目前是静态数据。
- 购买工具类商品会进入库存映射，但当前种植流程只消费种子类物品。
- 目前没有已提交的自动化测试。

## 开发建议

- 新增业务状态优先放入 `data/GameStore.kt` 或独立 repository，再通过 ViewModel 暴露给 UI。
- Composable 尽量保持展示职责，复杂状态流转放到 ViewModel 或数据层。
- 新增测试时可从 `GameStore` 的状态流转开始，例如购买、播种、浇水、加速和收获流程。
- 不要提交 `local.properties`、`.idea/`、`.gradle/`、`app/build/` 等机器或生成产物。

