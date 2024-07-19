pipeline {
    agent any
    environment {
        //默认使用流水线名称，如果您需要自定义，可以在这里手动修改
        PROJECT_NAME = currentBuild.fullDisplayName.take(30)
        // SCM项目源码默认的地址,如果您需要扫描的是其他目录，请手动修改指定
        PROJECT_PATH = pwd()
        //TOKEN是平台的访问权限令牌
        TOKEN = credentials('alipay-cybersec-token-key')
    }
    stages {
    stage('Download Antscasdk') {
            steps {
                    script {
                        // 使用wget下载antscasdk.jar文件到指定目录
                        sh "wget -q -O antscasdk.jar https://cybersec.alipay.com/api/sca/open/v1/client/download?token=${env.TOKEN}"
                    }
                }
            }
        stage('Alipay Scan') {
            steps {
                script {
                    def scanCommand = """
                        java -jar antscasdk.jar -p ${env.PROJECT_PATH} -n ${env.PROJECT_NAME} -t ${env.TOKEN}
                    """
                    // 执行扫描命令并获取输出
                    def scanOutput = sh(script: scanCommand, returnStdout: true).trim()
                    // 打印扫描结果到控制台
                    echo scanOutput
                    def startIndex = scanOutput.indexOf('漏洞结果:')
                    def filteredOutput = scanOutput.substring(startIndex + '漏洞结果:'.length())
                    // 解析JSON格式的输出;Jenkins需要安装Pipeline Utility Steps插件
                    def scanResults = readJSON text: filteredOutput
                    // 获取 bugTotal 的值
                    def bugTotal = scanResults.data.bugTotal
                    // 打印分享链接
                    echo "Scan Share Link: ${scanResults.data.shareLink}"
                }
            }
        }
    }
}
