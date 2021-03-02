pipeline {
    agent {
        docker {
            image 'openjdk:11.0.5-slim'
            args "-v /root/.m2:/root/.m2"
        }
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
                sh './mvnw compile'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
        stage('Package develop') {
            when {
                branch 'develop'
            }
            steps {
                emailext body: "Ссылка на результат ${env.BUILD_URL} develop hook1",
                        recipientProviders: [buildUser()],
                        subject: "Сборка develop",
                        attachLog: true,
                        compressLog: true
            }
        }
        stage('Package master') {
            when {
                branch 'master'
            }
            steps {
                sh './mvnw package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts 'target/*.war'
                    jacoco(
                            execPattern: 'target/*.exec',
                            classPattern: 'target/classes',
                            sourcePattern: 'src/main/java',
                            exclusionPattern: 'src/test*'
                    )
                    emailext body: "Ссылка на результат ${env.BUILD_URL}",
                            recipientProviders: [buildUser()],
                            subject: "Успешная сборка: ${currentBuild.fullDisplayName}",
                            attachLog: true,
                            compressLog: true
                }
            }
        }
    }
}