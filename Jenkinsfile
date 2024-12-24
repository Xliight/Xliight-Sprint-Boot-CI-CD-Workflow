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
        IMAGE_TAG="${RELEASE}-${BUILD_ID}"
        
    }
  
  stages {
      
    stage("Cleanup Workspace"){
                steps {
                cleanWs()
                }
    }  
     
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
                waitForQualityGate abortPipeline: false, credentialsId: 'sonarr'
        
            
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
   

     
    stage("Trigger CD Pipeline") {
            steps {
                script {
                     sh '''
                        curl -I -X POST "http://xlight:11d5ed8e74432c154e0001c43678872552@192.168.1.10:8080/job/CD-App%20Pipeline/buildWithParameters?token=gitops-token&IMAGE_TAG=${IMAGE_TAG}" -H "Jenkins-Crumb:04af1597a21004234e8bf778a76b167b6edc153859c8d78671656abd00b782e2"

                    '''
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
                        "content": "Build #${env.BUILD_NUMBER} CI has finished with status: ${buildStatus} ",
                        "username": "Jenkins Bot CI"
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
                        "content": "Build #${env.BUILD_NUMBER} CI has Failed with status: ${buildStatus} ",
                        "username": "Jenkins Bot CI"
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
              def jobName = env.JOB_NAME
              def buildNumber = env.BUILD_NUMBER
              def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
              def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'
  
              def body = """
                  <html>
                  <body>
                  <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                  <h2>${jobName} - Build ${buildNumber}</h2>
                  <div style="background-color: ${bannerColor}; padding: 10px;">
                  <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
                  </div>
                  <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
                  </div>
                  </body>
                  </html>
              """
  
              emailext (
                  subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                  body: body,
                  to: 'abdomostme@gmail.com',
                  from: 'jenkins@xlight.com',
                  replyTo: 'abdomostme@gmail.com',
                  mimeType: 'text/html',
                  attachmentsPattern: 'trivy-image-report.html'
              )
            }
        }
    }
}
