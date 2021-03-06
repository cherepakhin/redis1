pipeline {
    agent any
    environment {
        //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
    }

    stages {
        stage('Build') {
            agent {
                docker {
                    reuseNode true
                    image 'fabric8/java-alpine-openjdk11-jre'
                    args "-v /root/.m2:/root/.m2"
                }
            }
            steps {
                checkout scm
                sh './mvnw compile'
            }
        }
        stage('Test') {
            agent {
                reuseNode true
                docker {
                    image 'maven:3'
                    args "-v /var/run/docker.sock:/var/run/docker.sock"
                }
            }

            steps {
                sh "echo $PWD"
                sh 'mvn test'
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
        stage('Sonar') {
            agent {
                docker {
                    reuseNode true
                    image 'fabric8/java-alpine-openjdk11-jre'
                    args "-v /root/.m2:/root/.m2"
                }
            }
            steps {
                sh './mvnw sonar:sonar -Dsonar.projectKey=redis1 -Dsonar.host.url=http://192.168.1.20:9000 -Dsonar.login=c0aa07efb2c715621712fc9add4738a90d6f7bef'
            }
        }
        stage('JaCoCo') {
            agent {
                docker {
                    reuseNode true
                    image 'fabric8/java-alpine-openjdk11-jre'
                    args "-v /root/.m2:/root/.m2"
                }
            }
            steps {
                jacoco(
                        execPattern: 'target/*.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java',
                        exclusionPattern: 'src/test*'
                )
            }
        }
        stage('Package develop') {
            agent {
                docker {
                    reuseNode true
                    image 'fabric8/java-alpine-openjdk11-jre'
                    args "-v /root/.m2:/root/.m2"
                }
            }
            when {
                branch 'develop'
            }
            steps {
                emailext body: "Ссылка на результат ${env.BUILD_URL} develop hook2",
                        recipientProviders: [buildUser()],
                        subject: "Сборка develop",
                        attachLog: true,
                        compressLog: true
            }
        }
        stage('Package master') {
            agent {
                docker {
                    reuseNode true
                    image 'fabric8/java-alpine-openjdk11-jre'
                    args "-v /root/.m2:/root/.m2"
                }
            }
            when {
                branch 'master'
            }
            steps {
                sh './mvnw package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts 'target/*.war'
                    emailext body: "Ссылка на результат ${env.BUILD_URL}",
                            recipientProviders: [buildUser()],
                            subject: "Успешная сборка: ${currentBuild.fullDisplayName}",
                            attachLog: true,
                            compressLog: true
                    sh "curl -T \"target/redis1##${VERSION}.war\" \"http://deployer:pass@v.perm.ru:8080/manager/text/deploy?path=/redis1&update=true&version=${VERSION}\""
                }
            }
        }
    }
}