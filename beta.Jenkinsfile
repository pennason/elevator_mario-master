pipeline {
    agent any

    tools {
        maven 'maven-3.9.9'
        jdk 'JDK21'
    }

    options {
        gitLabConnection(gitLabConnection: 'GitLab connection', jobCredentialId: '')
        // sets the status of the pipeline depending on the pipeline _end_ results
        gitlabBuilds(builds: ["1.构建Jar包", "2.上传Jar包到服务器", "3.部署应用", "4.自动化测试"])
    }

    parameters {
        string(name: 'MODULE_NAMES', defaultValue: 'api,pm,camera,big-screen,fault,hik-cloud,hik-ys,camera-tysl,applets,sender,kafka-forward,eureka,gateway,spring-boot-admin,sa-token,socket',
            description: 'api: api模块\ncamera: 摄像头模块\ntpcamera: TPLink摄像头\nbig-screen: 通用大屏模块\nfault: 告警模块\nhik-cloud:云眸摄像头模块\nhik-ys: 萤石摄像头模块\ncamera-tysl: 天翼视联摄像头模块\npm: 项目管理模块\napplets：小程序模块\nsender: 北向推送模块\nkafka-forward: kafka消息转发\neureka: Eureka服务发现\ngateway:网关模块\nspring-boot-admin: Spring Boot Admin\nauth: 认证模块（废弃）\nsa-token:授权模块')
        string(name: 'PUBLISH_VERSION', defaultValue: '0.0.1-SNAPSHOT', description: '发布的版本')
    }

    stages {
        stage('1.构建Jar包') {
            steps {
                updateGitlabCommitStatus name: '1.构建Jar包', state: 'running'
                script {
                    TEMP_MODULE_NAMES = params.MODULE_NAMES
                    def moduleDefaultValues = "api,pm,camera,big-screen,fault,hik-cloud,hik-ys,camera-tysl,applets,sender,kafka-forward,eureka,gateway,spring-boot-admin,sa-token,socket"
                    def mavenParams = " -Dmaven.test.failure.ignore=true -Dmaven.test.skip=true -DskipTests=true"
                    //sh 'printenv'
                    //git branch: 'beta', url: 'git@gitlab.shmashine.com:platform/elevator_mario.git'
                    TEMP_COMMIT_MESSAGE = ""
                    if (TEMP_MODULE_NAMES == moduleDefaultValues) {
                        // 获取提交代码的message
                        def commitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                        echo "commitMessage: ${commitMessage}"
                        TEMP_COMMIT_MESSAGE = commitMessage
                        if (TEMP_COMMIT_MESSAGE.contains("nopub") || TEMP_COMMIT_MESSAGE.contains("no pub")) {
                            echo "不需要发版"
                            return "不需要发版"
                        }
                        if (commitMessage.contains("module")) {
                            TEMP_MODULE_NAMES = ""
                            if (commitMessage.contains("api")) {
                                TEMP_MODULE_NAMES += ",api"
                            }
                            if (commitMessage.contains("pm")) {
                                TEMP_MODULE_NAMES += ",pm"
                            }
                            if (commitMessage.contains("big-screen")) {
                                TEMP_MODULE_NAMES += ",big-screen"
                            }
                            if (commitMessage.contains("fault")) {
                                TEMP_MODULE_NAMES += ",fault"
                            }
                            if (commitMessage.contains("hik-cloud")) {
                                TEMP_MODULE_NAMES += ",hik-cloud"
                            }
                            if (commitMessage.contains("hik-ys")) {
                                TEMP_MODULE_NAMES += ",hik-ys"
                            }
                            if (commitMessage.contains("camera-tysl")) {
                                TEMP_MODULE_NAMES += ",camera-tysl"
                            }
                            if (commitMessage.contains("tpcamera")) {
                                TEMP_MODULE_NAMES += ",tpcamera"
                            }
                            if (commitMessage.contains("camera,") || commitMessage.contains(",camera ") || commitMessage.contains(" camera ") || commitMessage.endsWith("camera")) {
                                TEMP_MODULE_NAMES += ",camera"
                            }
                            if (commitMessage.contains("applets")) {
                                TEMP_MODULE_NAMES += ",applets"
                            }
                            if (commitMessage.contains("sender")) {
                                TEMP_MODULE_NAMES += ",sender"
                            }
                            if (commitMessage.contains("kafka-forward")) {
                                TEMP_MODULE_NAMES += ",kafka-forward"
                            }
                            if (commitMessage.contains("eureka")) {
                                TEMP_MODULE_NAMES += ",eureka"
                            }
                            if (commitMessage.contains("gateway")) {
                                TEMP_MODULE_NAMES += ",gateway"
                            }
                            if (commitMessage.contains("spring-boot-admin")) {
                                TEMP_MODULE_NAMES += ",spring-boot-admin"
                            }
                            if (commitMessage.contains("sa-token")) {
                                TEMP_MODULE_NAMES += ",sa-token"
                            }
                            if (commitMessage.contains("satoken")) {
                                TEMP_MODULE_NAMES += ",sa-token"
                            }
                            if (commitMessage.contains("socket")) {
                                TEMP_MODULE_NAMES += ",socket"
                            }
                            if (commitMessage.contains("auth")) {
                                TEMP_MODULE_NAMES += ",auth"
                            }
                            if (TEMP_MODULE_NAMES == "") {
                                TEMP_MODULE_NAMES = moduleDefaultValues
                            } else {
                                TEMP_MODULE_NAMES = TEMP_MODULE_NAMES.substring(1)
                            }
                        } else {
                            // 定义不需要发版关键字
                            TEMP_COMMIT_MESSAGE = TEMP_COMMIT_MESSAGE + " nopub"
                            echo "没有指定关键字：module，不需要发版"
                            return "没有指定关键字：module，不需要发版"
                        }
                        // 是否做代码检测
                        //if (commitMessage.contains("skip") && skipString.contains("test")) {
                        //    mavenParams = " -Dmaven.test.failure.ignore=true -Dmaven.test.skip=true -DskipTests=true"
                        //}
                        if (commitMessage.contains("skip") && (commitMessage.contains("checkstyle") || commitMessage.contains("all"))) {
                            mavenParams += " -Dcheckstyle.skip=true"
                        }
                    }
                    // 打包
                    configFileProvider([configFile(fileId: '662cd0e7-0ec9-44b4-807f-aa291bfa4842', variable: 'MAVEN_SETTINGS')]) {
                        sh "mvn -s $MAVEN_SETTINGS ${mavenParams} clean package  -Dcheckstyle.skip=true"
                    }
                    echo "打包完成"
                }
            }

            post {
                success {
                    updateGitlabCommitStatus name: '1.构建Jar包', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: '1.构建Jar包', state: 'failed'
                }
            }
        }

        stage('2.上传Jar包到服务器') {
            steps {
                updateGitlabCommitStatus name: '2.上传Jar包到服务器', state: 'running'
                script {
                    if (TEMP_COMMIT_MESSAGE.contains("nopub") || TEMP_COMMIT_MESSAGE.contains("no pub")) {
                        echo "不需要发版"
                        return "不需要发版"
                    }
                    def sshPublisherConfig = "mashine-local-125"
                    def transferPath = "transfer-files"
                    def remotePath = "/data/jars/iot/"
                    def moduleNames = TEMP_MODULE_NAMES
                    def publishVersion = params.PUBLISH_VERSION
                    echo '移动文件到目标服务器'
                    sh "mkdir -p ${transferPath}"
                    sh "rm -rf ${transferPath}/*.jar"

                    def moduleArrays = moduleNames.split(",").collect { it.trim() }
                    moduleArrays.each{item ->
                        def filePrefix = "${item}/target/${item}"
                        if ("applets" == item) {
                            filePrefix = "UserClientApplets/UserClientApplets-server/target/UserClientApplets-server"
                        } else if ("big-screen" == item) {
                            filePrefix = "commonBigScreen/commonBigScreen-server/target/commonBigScreen-server"
                        } else if ("hik-cloud" == item) {
                            filePrefix = "hik-yunmou/hik-yunmou-server/target/hik-yunmou-server"
                        } else if ("hik-ys" == item) {
                            filePrefix = "HKCameraByYS/HKCameraByYS-server/target/HKCameraByYS-server"
                        } else if ("camera-tysl" == item) {
                            filePrefix = "camera-tysl/camera-tysl-server/target/camera-tysl-server"
                        } else if ("tpcamera" == item) {
                            filePrefix = "tplink-camera/tplink-camera-server/target/tplink-camera-server"
                        } else if ("pm" == item) {
                            filePrefix = "projectManage/target/projectManage"
                        } else if ("hikv" == item) {
                            filePrefix = "hikvisionPlatform/hikvPlatform-server/target/hikvPlatform-server"
                        } else if ("sa-token" == item || "satoken" == item) {
                            filePrefix = "sa-token/sa-token-springboot/target/sa-token-springboot"
                        }

                        def fileName = "${filePrefix}-${publishVersion}.jar"
                        sh "mv ${fileName} ${transferPath}"
                    }

                    // 使用 Jenkins 的 SSH 插件或凭证进行文件传输
                    sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "${remotePath}", removePrefix: "${transferPath}", sourceFiles: "${transferPath}/*.jar", execCommand: "", excludes: "", execTimeout: 120000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
    				echo '传输文件完成'
                }
            }

            post {
                success {
                    updateGitlabCommitStatus name: '2.上传Jar包到服务器', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: '2.上传Jar包到服务器', state: 'failed'
                }
            }
        }

        stage('3.部署应用') {
            steps {
                updateGitlabCommitStatus name: '3.部署应用', state: 'running'
                script {
                    if (TEMP_COMMIT_MESSAGE.contains("nopub") || TEMP_COMMIT_MESSAGE.contains("no pub")) {
                        echo "不需要发版"
                        return "不需要发版"
                    }
                    def sshPublisherConfig = "mashine-local-125"
                    def remoteShell = "/data/apps/auto-publish-mario.sh"
                    def moduleNames = TEMP_MODULE_NAMES
                    def moduleArrays = moduleNames.split(",").collect { it.trim() }
                    echo '重启相关服务'
                    moduleArrays.each{item ->
                        def execCommandFile = "${remoteShell} ${item}";
                        // 使用 Jenkins 的 SSH 插件或凭证进行文件传输
                        sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "", removePrefix: "", sourceFiles: "", execCommand: "${execCommandFile}", excludes: "", execTimeout: 3600000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    }
                    echo '服务部署完成'
                }
            }

            post {
                success {
                    updateGitlabCommitStatus name: '3.部署应用', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: '3.部署应用', state: 'failed'
                }
            }
        }

        stage('4.自动化测试') {
            steps {
                updateGitlabCommitStatus name: '4.自动化测试', state: 'running'
                script {
                    if (TEMP_COMMIT_MESSAGE.contains("nopub") || TEMP_COMMIT_MESSAGE.contains("no pub")) {
                        echo "不需要发版"
                        return "不需要发版"
                    }
                    //def sshPublisherConfig = "mashine-local-185"
                    //def remoteShell = "/data/apps/elevator_mario_autotest.sh"
                    echo '暂无自动化测试脚本'
                    //sshPublisher(publishers: [sshPublisherDesc(configName: "${sshPublisherConfig}", transfers: [sshTransfer(remoteDirectory: "", removePrefix: "", sourceFiles: "", execCommand: "${remoteShell}", excludes: "", execTimeout: 120000, flatten: false, makeEmptyDirs: true, patternSeparator: '[, ]+', remoteDirectorySDF: false)], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])
                    echo '自动化测试完成'
                }
            }

            post {
                success {
                    updateGitlabCommitStatus name: '4.自动化测试', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: '4.自动化测试', state: 'failed'
                }
            }
        }
    }
}
