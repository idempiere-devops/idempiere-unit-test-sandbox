pipeline {
    agent {
        docker { image 'eclipse-temurin:17-jdk' }
    }

    stages {
        stage('Test') {
            steps {
                echo 'Hello World!'
                sh 'java --version'
            }
        }
    }
}
