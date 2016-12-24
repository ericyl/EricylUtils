# Changelog

## 1.0.x
* init
* 封装所有 Utils
* 整合 Table Report 功能
* 整合 Java WebService 网络交互框架

## 1.1.x
* 新增 RecyclerView Support, 支持多 Type 模式(Footer & Header)

## 1.2.*
* 新增 Volley Support, 支持 Self Sign SSL

## 1.3.*
* 新增 Barcode 长按识别相关代码

## 1.4.*
* 新增 AES 加解密相关代码

## 1.5.*
* 新增 `CustomSearchView` 自定义 SearchView

## 1.6.*
* 新增 `CustomNavigationView` 自定义 NavigationView

## 2.6.*
* 代码重构
* 重构 ProGuard rules 混淆代码
* 新增 `PhoneManufacturerUtils` 代码

## 2.7.*
* 修改 `CustomNavigationView` 为 `CustomTransNavNavigationView`, 并更新少许逻辑
* 新增 `CustomTransNavWithPaddingStatusBarFrameLayout` 用于 android:windowTranslucentNavigation = true 时 Padding 出 StatusBar 的位置
* Android 7.0 多窗口模式下有 BUG 后期修复, 暂时放弃 TransNavigationBar 等 Google 全面适配更新

## 2.8.*

### 2.8.99
* 更新 `DownloadUtils` 相关代码

### 2.8.100
* 更新 ksoap2-android version: 2.4 -> 3.6.2

### 2.8.101
* 新增 Bitmap LruCache 的相关代码
* 发布到 JCenter 和 Maven 中央服务器

### 2.8.102
* 更新 Maven pom.xml 生成规则

### 2.8.103
* 新增 `CircleTextView`
* 新增 Button default drawable style
* 引入 `NumberPickerPreference` 并修改 (原 Package: com.cyanogenmod.trebuchet.preference)
GitHub: [Java File](https://github.com/CyanogenMod/android_packages_apps_Trebuchet/blob/cm-10.2/src/com/cyanogenmod/trebuchet/preference/NumberPickerPreference.java) / [XML File](https://github.com/CyanogenMod/android_packages_apps_Trebuchet/blob/cm-10.2/res/layout/number_picker_dialog.xml) / [Attributes](https://github.com/CyanogenMod/android_packages_apps_Trebuchet/blob/cm-10.2/res/values/attrs.xml#L158)

### 2.8.104
* 新增 `AndroidAnnotationUtils` 注解类
* 更新 OpenFile 相关方法，适配 android N 以上设备

## 2.9.*

### 2.9.105
* 拆分 web service 网络交互框架, 详情见`com.ericyl.erbservice:webservice:1.1.2` / `com.ericyl.erbservice:webservice2:2.0.1`

### 2.9.106
* 新增 `ColorUtils`
* 新增 `CircleTextView` 参数 `singleText`
* 修改 `BarCodeDecoderUtils` 名称为 `BarcodeDecoderUtils`


### 2.9.107
* 更新 `FileUtils`, `PermissionUtils`, `ShowInfoUtils` 等方法

## 2.10.*

### 2.10.0
* 新增 `RadioButtonPreference`, `RadioGroupPreference`
* 更新 `TransNavigationView`, `TransStatusBarFrameLayout` 等类
* 修复 Bugs

### 2.10.1
* 完善 `RadioGroupPreference` 方法

## 2.11.*

### 2.11.1
* 删除 `ActivityUtils`
* 更新 `BarcodeDecoderUtils`, `FileUtils` 等方法
* 重构 RecyclerView 模块, 不兼容旧版本
* 更新 `NumberPickerPreference` 可配置手动输入
* 废弃 `CustomScrollView`, `CustomWebView`, `NoScrollerListView` 等类
* 更新 AppCompat Support Library 包版本为 25.1.0

### 2.11.2
* 新增 LoadingLayout

### 2.11.3
* 新增 LoadStatus 状态
* 修改 LoadingLayout
* 拆分 AndroidAnnotationUtils 之新 `annotation` 包名
* 更新 DownloadUtils 方法

#### 2.11.4
* `CircleTextView` 修改 Bugs
* `OnRecyclerViewPinnedViewListener` 更新方法名
* 更新 AES 密钥生成代码，适配 android N
* 删除 JCenter 发布代码

#### 2.11.5
* 更新 SowInfoUtils 为 StringUtils, 并删除 Toast & Snackbar 使用代码
