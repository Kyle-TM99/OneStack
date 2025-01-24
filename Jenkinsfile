pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE = 'docker-compose'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }
        
        stage('Prepare Environment') {
            steps {
                sh '''
                    mkdir -p /usr/share/nginx/html/images
                    sudo chown -R $(id -u):$(id -g) /usr/share/nginx/html/images
                    chmod -R 755 /usr/share/nginx/html/images
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    ${DOCKER_COMPOSE} down --volumes=false || true
                    ${DOCKER_COMPOSE} up -d --build
                '''
            }
        }
        
        stage('Health Check') {
            steps {
                sh '${DOCKER_COMPOSE} ps'
            }
        }
    }
    
    post {
        failure {
            sh '${DOCKER_COMPOSE} logs'
        }
        always {
            sh 'docker system prune -f --volumes=false'
        }
    }
} 