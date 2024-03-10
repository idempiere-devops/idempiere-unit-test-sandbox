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
                sh '/source/idempiere-target-platform-plugin/plugin-builder ./org.idempiere.sandbox'
            }
        }
    }
}
