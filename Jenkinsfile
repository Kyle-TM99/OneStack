pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE = '/usr/local/bin/docker-compose'
    }
    
    stages {
        stage('Install Dependencies') {
            steps {
                sh '''
                    # docker-compose 설치 (없는 경우에만)
                    if ! command -v docker-compose &> /dev/null; then
                        sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                        sudo chmod +x /usr/local/bin/docker-compose
                    fi
                '''
            }
        }
        
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
            sh '''
                if command -v ${DOCKER_COMPOSE} &> /dev/null; then
                    ${DOCKER_COMPOSE} logs
                fi
            '''
        }
        always {
            sh 'docker system prune -f --volumes=false'
        }
    }
} 