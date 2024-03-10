pipeline {
    agent {
        docker {
            image 'idempiereofficial/idempiere:source-release-11.0'
        }
    }
    stages {
        stage('Compile') {
            steps {
                dir("/source/idempiere-target-platform-plugin"){
                    sh "./plugin-builder ${WORKSPACE}/org.idempiere.sandbox"
                }
            }
        }
    }
}
