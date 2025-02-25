pipeline {
    agent any    
    tools {
        maven "Maven"
    }
    stages {
        stage('Cloning repo') {
            steps {
                script {
                    echo 'Cloning the repo'
                    sh 'git clone https://github.com/zainabzulfiqar06/simple-java-maven-app.git'
                }
            }

        }
        stage('Version Increment') {
            steps {
                script {
                    echo 'Incrementing application version...'
                    dir('simple-java-maven-app') { 
                        sh '''
                        #!/bin/bash
                        mvn build-helper:parse-version versions:set \
                            -DnewVersion="$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout | awk -F. '{print $1"."$2"."$3+1}')" \
                            versions:commit
                        '''
                    }
                }
            }
        }   
        stage('Build app') {
            steps {
                script {
                    if (!fileExists('simple-java-maven-app')) {
                        sh 'git clone https://github.com/zainabzulfiqar06/simple-java-maven-app.git'
                    } else {
                        echo "Repository already cloned, skipping cloning."
                    }
                }
                dir('simple-java-maven-app') { 
                    echo "Building app"
                    sh 'mvn clean package'
                }
            }
        }
        stage('Building image') {
            steps {
                script{
                    echo 'Checking if Dockerfile exists...'
                    sh 'ls -l simple-java-maven-app/'  

                    echo 'Building the image'
                     dir('simple-java-maven-app') {  
                         sh 'docker build -t java-maven-app .'
                     }
                }
            }
        }
        stage('Pushing to docker hub') {
            steps {
                script{
                    echo 'Pushing the image to docker hub'
                    withCredentials([usernamePassword(credentialsId:"dockerhub",passwordVariable:"dockerhubpass",usernameVariable:"dockerhubuser")]){
                    sh "docker tag java-maven-app ${env.dockerhubuser}/java-maven-app:latest"
                    sh "docker login -u ${env.dockerhubuser} -p ${env.dockerhubpass}"  
                    sh "docker push ${env.dockerhubuser}/java-maven-app:latest "
                    }
                }
            }
        } 
    }
}
