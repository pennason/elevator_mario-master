pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
        jdk 'JDK21'
    }

    parameters {
        string(name: 'MODULE_NAMES', defaultValue: 'api,pm,camera,tpcamera,big-screen,fault,hik-cloud,hik-ys,camera-tysl,applets,sender,kafka-forward,eureka,gateway,spring-boot-admin,sa-token,auth',
            description: 'api: api模块\ncamera: 摄像头模块\ntpcamera: TPLink摄像头\nbig-screen: 通用大屏模块\nfault: 告警模块\nhik-cloud:云眸摄像头模块\nhik-ys: 萤石摄像头模块\ncamera-tysl: 天翼视联摄像头模块\npm: 项目管理模块\napplets：小程序模块\nsender: 北向推送模块\nkafka-forward: kafka消息转发\neureka: Eureka服务发现\ngateway:网关模块\nspring-boot-admin: Spring Boot Admin\nauth: 认证模块（废弃）\nsa-token:授权模块')
        string(name: 'PUBLISH_VERSION', defaultValue: '0.0.1-SNAPSHOT', description: '发布的版本')
        string(name: 'PUBLISH_BRANCH', defaultValue: 'release', description: '发布的分支')
    }

    stages {
        stage('1.构建Jar包') {
            steps {
                script {
                    //sh 'printenv'
                    //git branch: 'release', url: 'git@gitlab.shmashine.com:platform/elevator_mario.git'
                    // 打包
                    def sshPublisherConfig = "ali-nps-package"
                    def remoteShell = "/data/gitlab/mvn-project.sh elevator_mario ${params.PUBLISH_BRANCH}"
                    sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "", removePrefix: "", sourceFiles: "", execCommand: "${remoteShell}", excludes: "", execTimeout: 1200000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    echo "打包完成"
                }
            }
        }

        stage('2.上传Jar包到服务器') {
            steps {
                script {
                    TEMP_MODULE_NAMES = params.MODULE_NAMES
                    def sshPublisherConfig = "ali-nps-package"
                    def remotePath = "/data/share/jars/iot"
                    def moduleNames = TEMP_MODULE_NAMES
                    def publishVersion = params.PUBLISH_VERSION
                    echo '移动文件到目标服务器'
                    def execCommandFile = "/data/gitlab/iot-copy-jar-from-ali-gitlab-package.sh ${moduleNames} ${publishVersion}";
                    // 使用 Jenkins 的 SSH 插件或凭证进行文件传输
                    sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "${remotePath}", removePrefix: "", sourceFiles: "", execCommand: "${execCommandFile}", excludes: "", execTimeout: 1200000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    //if (moduleNames.contains("socket")) {
                    //    sshPublisherConfig = "ali-netty-47.104.215.210"
                    //    moduleNames = "socket"
                    //    execCommandFile = "/data/apps/iot-copy-jar-from-ali-gitlab-package.sh ${moduleNames} ${publishVersion}";
                    //    // 使用 Jenkins 的 SSH 插件或凭证进行文件传输
                    //    sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "${remotePath}", removePrefix: "", sourceFiles: "", execCommand: "${execCommandFile}", excludes: "", execTimeout: 1200000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    //}

                    echo '传输文件完成'
                }
            }
        }

        stage('3.部署应用') {
            steps {
                script {
                    TEMP_MODULE_NAMES = params.MODULE_NAMES
                    def sshPublisherConfig = "ali-nps-package"
                    def remoteShell = "/data/share/apps/iot/auto-publish.sh"
                    def moduleNames = TEMP_MODULE_NAMES
                    def publishVersion = params.PUBLISH_VERSION
                    def moduleArrays = moduleNames.split(",").collect { it.trim() }
                    echo '部署相关应用'
                    moduleArrays.each{item ->
                        def execCommandFile = "${remoteShell} ${item} ${publishVersion}";
                        // 使用 Jenkins 的 SSH 插件或凭证进行文件传输
                        echo "${execCommandFile}"
                        sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "", removePrefix: "", sourceFiles: "", execCommand: "${execCommandFile}", excludes: "", execTimeout: 3600000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    }
                    echo '服务部署完成'
                }
            }
        }
    }
}
