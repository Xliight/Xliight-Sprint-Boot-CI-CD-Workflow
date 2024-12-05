pipeline {
  agent any
  
  tools{
      jdk 'jdk17'
      maven 'maven3'
  }


   environment {
        APP_NAME = "ouitripsapp"
        RELEASE = "1.0.0"
        DOCKER_USER = "xlight7"
        DOCKER_PASS = 'dockerhub'
        IMAGE_NAME = "${DOCKER_USER}/${APP_NAME}"
        //IMAGE_TAG="${RELEASE}-${BUILD_ID}"
        IMAGE_TAG=1.0.0-33
        JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")
    }
  
  stages {
      
    stage("Cleanup Workspace"){
                steps {
                cleanWs()
                }
    }  
    /**  
    stage('Checkout') {
      steps {
        git branch: 'main', changelog: false, credentialsId: 'git_token', poll: false, url: 'https://github.com/Xliight/Xliight-Sprint-Boot-CI-CD-Workflow.git'
      }
    }
    stage('Build and Test') {
      steps {
        sh 'mvn clean package'
      }
    }
    stage('Static Code Analysis') {

      steps {
          script{
                withSonarQubeEnv(credentialsId: 'sonarr') {

                sh ''' mvn sonar:sonar'''        
            }
         }
        
      }
    }
    
    stage('Quality Gate') {
      steps {
                waitForQualityGate abortPipeline: true, credentialsId: 'sonarr'
        
            
        }
    }
    
    
    stage('Build & Tag Docker Image') {
            steps {
               script {
                   withDockerRegistry(credentialsId: DOCKER_PASS, toolName: 'docker') {
                            sh "docker build -t ${IMAGE_NAME} ."
                            sh "docker tag ${IMAGE_NAME} ${IMAGE_NAME}:${IMAGE_TAG}"
                    }
               }
            }
        }
    stage('Push Docker Image') {
            steps {
               script {
                   withDockerRegistry(credentialsId: DOCKER_PASS, toolName: 'docker') {
                            sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                            sh "docker push ${IMAGE_NAME}:latest"

                            
                    }
               }
            }
    }    
        
    stage('Docker Image Scan') {
            steps {
                sh "trivy image --format table -o trivy-image-report.html ${IMAGE_NAME}:latest "
            }
    }

    stage ('Cleanup Artifacts') {
          steps {
              script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker rmi ${IMAGE_NAME}:latest"
              }
          }
    }
   **/

     
    stage("Trigger CD Pipeline") {
            steps {
                script {
                   sh 'curl -I -X POST "http://xlight:11eb99d6a4d2ecb26e9fffc8caa41875d0@192.168.1.10:8080/job/CD-OuitripsApp/buildWithParameters?token=gitops-token&IMAGE_TAG=${IMAGE_TAG}" -H "Jenkins-Crumb:899c2cf4a935c24b4117cd049eeb549989734753264c1ed663dcdddd69826a99"'

                }
            }
    }
  }

   post {
        success {
            script {
                def discordWebhookUrl = 'https://discord.com/api/webhooks/1313951323116404796/GqaDHcd_-bCUcseTb4RXXKFIUsvpZHy2qp_6nfU-IBNEr1SJhPHhIbr9U6g4cRqPqVNu'
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def message = """
                    {
                        "content": "Build #${env.BUILD_NUMBER} CD has finished with status: ${buildStatus} ",
                        "username": "Jenkins Bot"
                    }
                """
                httpRequest acceptType: 'APPLICATION_JSON', 
                            contentType: 'APPLICATION_JSON', 
                            url: discordWebhookUrl, 
                            httpMode: 'POST', 
                            requestBody: message
            }
        }
        failure {
            script {
                def discordWebhookUrl = 'https://discord.com/api/webhooks/1313951323116404796/GqaDHcd_-bCUcseTb4RXXKFIUsvpZHy2qp_6nfU-IBNEr1SJhPHhIbr9U6g4cRqPqVNu'
                def buildStatus = currentBuild.result ?: 'FAILURE'
                def message = """
                    {
                        "content": "Build #${env.BUILD_NUMBER} CD has Failed with status: ${buildStatus} ",
                        "username": "Jenkins Bot"
                    }
                """
                httpRequest acceptType: 'APPLICATION_JSON', 
                            contentType: 'APPLICATION_JSON', 
                            url: discordWebhookUrl, 
                            httpMode: 'POST', 
                            requestBody: message
            }
        }
        
        always {
            script {
                echo "Build completed with result: ${currentBuild.result ?: 'SUCCESS'}"
            }
        }
    }
}
