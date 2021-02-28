pipeline {
    agent any

    options {
        skipStagesAfterUnstable()
        skipDefaultCheckout()
    }

    environment {
        //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
    }

    stages {
        stage('Build') {
            steps {
                checkout scm
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts 'target/*.war'
                    mail(to: 'vasi.che@gmail.com', subject: "Успешная сборка: ${currentBuild.fullDisplayName}", body: "Ссылка на результат ${env.BUILD_URL}")
                    sh "curl -T \"target/redis1##${VERSION}.war\" " + "\"http://deployer:pass@v.perm" + ".ru:8080/manager/text/deploy?path=/redis1&update=true\""
                }
            }
        }
    }
}