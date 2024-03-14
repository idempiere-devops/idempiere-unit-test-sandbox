pipeline {
    agent none
    environment {
        PLUGIN_NAME = 'org.idempiere.sandbox'
        IDEMPIERE_VERSION = '11.0.0'
        JAR_NAME = "${PLUGIN_NAME}-${IDEMPIERE_VERSION}.${BUILD_NUMBER}.jar"
    }
    stages {
        stage('Compile') {
            agent {
                docker {
                    image 'idempiereofficial/idempiere:source-release-11.0'
                }
            }
            steps {
                dir('target-platform') {
                    git branch: '11.0', url: 'https://github.com/ingeint/idempiere-target-platform-plugin.git'
                    sh './plugin-builder build ../${PLUGIN_NAME} ../${PLUGIN_NAME}.test'
                    archiveArtifacts artifacts: "target/${JAR_NAME}", fingerprint: true
                }
            }
        }
        stage('Deploy') {
            agent {
                node {
                    label 'idempiere'
                }
            }
            steps {
                copyArtifacts filter: "target/${JAR_NAME}", fingerprintArtifacts: true, projectName: "${JOB_NAME}", selector: specific("${BUILD_NUMBER}")
                sh 'cp target/${JAR_NAME} /home/jenkins/plugins'
                dir('deployer') {
                    git branch: 'master', url: 'https://github.com/ingeint/idempiere-plugin-deployer.git'
                    sh './deployer.sh deploy -h 127.0.0.1 -p 12612 -n ${PLUGIN_NAME} -l 5 -j /home/jenkins/plugins/${JAR_NAME}'
                }
            }
        }
    }
}
