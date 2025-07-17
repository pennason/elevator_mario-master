# 开发环境配置

## 开发环境

- OpenJDK 17

- Intellij IDEA

    - java编译器增加 `-parameters` 参数
        - Settings -> Build, Execution, Deployment -> Compiler -> Java Compiler -> Additional command line parameters
    - 启用 `Annotation Processors`.
        - Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable annotation processors
    - 插件
        - [MapStruct Support](https://plugins.jetbrains.com/plugin/10036-mapstruct-support)
        - [PlantUML integration](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
        - [AsciiDoc](https://plugins.jetbrains.com/plugin/7391-asciidoc)
        - [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea)

- Docker

## IDE代码样式配置

下载可用于 Intellij IDEA 的
[代码样式配置](intellij-java-codestyle.xml) 项目跟目录下有此文件 并导入

```
Settings -> Editor -> Code Style -> java -> Schema -> Import Schema -> Intellij IDEA code style XML
```

## 使用Checkstyle插件

Checkstyle插件的配置文件: [checkstyle.xml](checkstyle.xml)

- Settings -> Tools -> CheckStyle
    - `Checkstyle Version` 保持默认即可
    - `Scan Scope` 选 `Only Java sources (including tests)`
    - Configration file -> + -> 导入 checkstyle.xml

## 打包本地应用

#### 编译， 使用 VS 的 x64 Native Tools Command Prompt for VS 20XX

    mvn -Pnative native:compile

#### 打包成docker镜像

```xml

<properties>
    <spring-boot.build-image.imageName>xxx{名字中不能使用大写字母}xx</spring-boot.build-image.imageName>
</properties>
```

```text
mvn -Pnative spring-boot:build-image
```

#### 打包exe文件

- javac -d . src/com/xxx/Application.java
- native-image com.xxx.Application