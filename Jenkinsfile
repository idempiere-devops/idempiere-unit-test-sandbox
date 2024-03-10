pipeline {
    agent {
        docker {
            image 'idempiereofficial/idempiere:source-release-11.0'
        }
    }
    stages {
        stage('Compile') {
            steps {
                git branch: '11.0', url: 'https://github.com/ingeint/idempiere-target-platform-plugin.git'
                dir("idempiere-target-platform-plugin"){
                    sh "ls"
                    sh "./plugin-builder ${WORKSPACE}/org.idempiere.sandbox"
                }
            }
        }
    }
}
