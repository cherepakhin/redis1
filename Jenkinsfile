pipeline {
  agent any
  stages {
    stage('Source') {
      steps {
        git 'https://github.com/cherepakhin/redis1.git'
      }
    }

    stage('test&package') {
      post {
        success {
          junit '**/target/surefire-reports/TEST-*.xml'
          archiveArtifacts 'target/*.war'
          mail(to: 'vasi.che@gmail.com', subject: "Успешная сборка: ${currentBuild.fullDisplayName}", body: "Ссылка на результат ${env.BUILD_URL}")
          sh "curl -T \"target/redis1##0.0.1-SNAPSHOT.war\" "+"\"http://deployer:pass@v.perm"+".ru:8080/manager/text/deploy?path=/redis1&update=true\""
        }

      }
      steps {
        sh 'mvn clean package'
      }
    }

  }
}