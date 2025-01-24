pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE = '/usr/local/bin/docker-compose'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh '''
                    # gradlew 파일에 실행 권한 부여
                    chmod +x ./gradlew
                    ./gradlew clean build -x test
                '''
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
                    # 이전 컨테이너 중지
                    ${DOCKER_COMPOSE} down --volumes=false || true
                    
                    # 컨테이너 시작
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