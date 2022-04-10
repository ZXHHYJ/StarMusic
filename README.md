<p align="center"><img src="https://images.gitee.com/uploads/images/2021/0928/190530_2f2d830a_8555846.png" height="25%" width="25%"/></p>

<p align="center">
    <strong>Hello Anna!</strong>
    <br>
    <br>
    <a href="https://gitee.com/liuxiaoliu66/anna/wikis">Wiki</a>
</p>

<p align="center">
<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
<img src="https://img.shields.io/badge/license-Apache-blue"/>
<a href="https://gitee.com/liuxiaoliu66/anna/releases"><img src="https://img.shields.io/badge/updates-%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97-brightgreen"/></a>
</p>

<br>
<p align="center"><strong>本项目将保持维护状态很长时间，很长...</strong></p>
<br>

<br>
<p align="center"><strong>欢迎贡献代码/问题</strong></p>
<br>

- 高内聚低耦合
- 面向对象
- 高性能
- 自动化

<br>

## 功能

- 支持Okhttp的绝大部分请求
- 快速创建解析json并填充实体类
- 支持嵌套解析
- 支持自动装载至List中

## 安装

添加远程仓库根据创建项目的 Android Studio 版本有所不同

Android Studio Arctic Fox以下创建的项目 在项目根目录的 build.gradle 添加仓库

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Android Studio Arctic Fox以上创建的项目 在项目根目录的 settings.gradle 添加仓库

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

然后在 module 的 build.gradle 添加依赖框架

```groovy

dependencies {
    //...
    implementation 'com.gitee.liuxiaoliu66:anna:2.1.1'
}
```



## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
