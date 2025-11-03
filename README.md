# AndroidShortcutBrowser

兼容 Android 16 的浏览器类应用，支持创建网页快捷方式并全屏浏览。

## 功能
- 主界面输入网址，点击安装按钮后获取 favicon 并创建快捷方式。
- 快捷方式启动时全屏打开对应网页。
- 支持 Android 4.1（API 16）及以上。

## 目录结构
- app/src/main/java/...：主代码
- app/src/main/res/：资源文件
- app/build.gradle：模块构建配置
- build.gradle：项目构建配置

## 编译与测试
- 推荐使用 GitHub Actions 远程编译 APK。
- 可通过 adb 安装到 WSA 进行测试。
