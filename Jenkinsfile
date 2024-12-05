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
        JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")
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
   
    stage('Notify Discord') {
            steps {
                script {
                    def discordWebhookUrl = 'https://discord.com/api/webhooks/1313951323116404796/GqaDHcd_-bCUcseTb4RXXKFIUsvpZHy2qp_6nfU-IBNEr1SJhPHhIbr9U6g4cRqPqVNu'
                    def buildStatus = currentBuild.result ?: 'SUCCESS' // Default to 'SUCCESS' if no result is set

                    def message = """
                        {
                            "content": "Build #${env.BUILD_NUMBER} has finished with status: ${buildStatus} ",
                            "username": "Jenkins :) "
                        }
                    """
                    // Sending POST request to Discord
                    httpRequest acceptType: 'APPLICATION_JSON', 
                                contentType: 'APPLICATION_JSON', 
                                url: discordWebhookUrl, 
                                httpMode: 'POST', 
                                requestBody: message
                }
            }
    }
     
    stage("Trigger CD Pipeline") {
            steps {
                script {
                   sh "curl -v -k --user clouduser:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'http://192.168.1.10:8080/job/CD-OuitripsApp/buildWithParameters?token=gitops-token'"

                }
            }
    }

    // stage('Update Deployment File') {
    //     environment {
    //         GIT_REPO_NAME = "Jenkins-Zero-To-Hero"
    //         GIT_USER_NAME = "iam-veeramalla"
    //     }
    //     steps {
    //         withCredentials([string(credentialsId: 'github', variable: 'GITHUB_TOKEN')]) {
    //             sh '''
    //                 git config user.email "abhishek.xyz@gmail.com"
    //                 git config user.name "Abhishek Veeramalla"
    //                 BUILD_NUMBER=${BUILD_NUMBER}
    //                 sed -i "s/replaceImageTag/${BUILD_NUMBER}/g" java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
    //                 git add java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
    //                 git commit -m "Update deployment image to version ${BUILD_NUMBER}"
    //                 git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:main
    //             '''
    //         }
    //     }
    }
  }
