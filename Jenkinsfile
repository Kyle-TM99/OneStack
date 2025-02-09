pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE = '/usr/local/bin/docker-compose'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
        
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh '''
                    chmod +x ./gradlew
                    ./gradlew clean build -x test
                '''
            }
        }
        
        stage('Prepare Environment') {
            steps {
                sh '''
                    sudo mkdir -p /usr/share/nginx/html/images
                    sudo chown -R $(id -u):$(id -g) /usr/share/nginx/html/images
                    sudo chmod -R 755 /usr/share/nginx/html/images
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'GOOGLE_CLIENT_ID', variable: 'GOOGLE_CLIENT_ID'),
                    string(credentialsId: 'GOOGLE_CLIENT_SECRET', variable: 'GOOGLE_CLIENT_SECRET'),
                    string(credentialsId: 'GOOGLE_REDIRECT_URI', variable: 'GOOGLE_REDIRECT_URI'),
                    string(credentialsId: 'KAKAO_CLIENT_ID', variable: 'KAKAO_CLIENT_ID'),
                    string(credentialsId: 'KAKAO_REDIRECT_URI', variable: 'KAKAO_REDIRECT_URI'),
                    string(credentialsId: 'GMAIL_USERNAME', variable: 'GMAIL_USERNAME'),
                    string(credentialsId: 'GMAIL_PASSWORD', variable: 'GMAIL_PASSWORD'),
                    string(credentialsId: 'PORTONE_API_KEY', variable: 'PORTONE_API_KEY'),
                    string(credentialsId: 'PORTONE_API_SECRET', variable: 'PORTONE_API_SECRET')
                ]) {
                    sh '''
                        # 기존 컨테이너 정리
                        ${DOCKER_COMPOSE} down --volumes=false || true
                        docker system prune -f
                        
                        # 새로운 이미지 빌드 및 컨테이너 시작
                        ${DOCKER_COMPOSE} build --no-cache
                        ${DOCKER_COMPOSE} up -d
                        
                        # MySQL 컨테이너 준비 대기
                        echo "Waiting for MySQL to be ready..."
                        for i in $(seq 1 30); do
                            if docker exec myone mysqladmin ping -h localhost -u root -pKyle9907! >/dev/null 2>&1; then
                                echo "MySQL is ready"
                                break
                            fi
                            if [ $i -eq 30 ]; then
                                echo "MySQL failed to start"
                                exit 1
                            fi
                            echo "Waiting for MySQL... $i/30"
                            sleep 10
                        done
                        
                        # 애플리케이션 컨테이너 상태 확인
                        echo "Checking application status..."
                        for i in $(seq 1 12); do
                            if docker ps | grep onestack | grep -q "Up"; then
                                echo "Application is running"
                                break
                            fi
                            if [ $i -eq 12 ]; then
                                echo "Application failed to start"
                                exit 1
                            fi
                            echo "Waiting for application... $i/12"
                            sleep 10
                        done
                    '''
                }
            }
        }
        
        stage('Health Check') {
            steps {
                sh '''
                    echo "Container Status:"
                    ${DOCKER_COMPOSE} ps
                    
                    echo "MySQL Logs:"
                    docker logs myone --tail 50
                    
                    echo "Application Logs:"
                    docker logs onestack --tail 50
                '''
            }
        }
    }
    
    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            sh '''
                echo "Deployment failed. Collecting logs..."
                ${DOCKER_COMPOSE} logs
                ${DOCKER_COMPOSE} down
            '''
        }
        always {
            sh 'docker system prune -f --volumes=false'
        }
    }
} 