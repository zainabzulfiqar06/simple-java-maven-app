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
                    sh "git clone https://github.com/zainabzulfiqar06/simple-java-maven-app.git"
            
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
                        def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                        def version = matcher[0][1]
                        env.IMAGE_NAME="$version-$BUILD_NUMBER"
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
        stage('building docker image'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId:"dockerhub",passwordVariable:"dockerhubpass",usernameVariable:"dockerhubuser")]){
                    sh "docker tag java-maven-app ${env.dockerhubuser}/java-maven-app:$IMAGE_NAME "
                    sh "docker login -u ${env.dockerhubuser} -p ${env.dockerhubpass}"  
                    sh "docker push ${env.dockerhubuser}/java-maven-app:$IMAGE_NAME "
                    sh "docker stop java-maven-app || true"
                    sh "docker rm java-maven-app || true"
                    sh "docker run -d --name java-maven-app -p 9090:9090 ${env.dockerhubuser}/java-maven-app:$IMAGE_NAME"
                    echo "Container started successfully!"
                    }
                }
            }
        }
    }
}
