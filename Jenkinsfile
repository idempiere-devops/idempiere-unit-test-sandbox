pipeline {
    agent none
    environment {
        PLUGIN_NAME = 'org.idempiere.sandbox'
        IDEMPIERE_VERSION = '11.0.0'
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
                    archiveArtifacts artifacts: "target/${PLUGIN_NAME}-${IDEMPIERE_VERSION}.${BUILD_NUMBER}.jar", fingerprint: true
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
                copyArtifacts filter: "target/${PLUGIN_NAME}-${IDEMPIERE_VERSION}.${BUILD_NUMBER}.jar", fingerprintArtifacts: true, projectName: env.JOB_NAME, selector: specific(env.BUILD_NUMBER)
            }
        }
    }
}
