pipeline {
    agent {
        docker {
            image 'idempiereofficial/idempiere:source-release-11.0'
            args '--entrypoint='
        }
    }
    stages {
        stage('Compile') {
            steps {
                sh 'ls'
            }
        }
    }
}
