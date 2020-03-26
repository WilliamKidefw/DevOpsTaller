backendVersion = '0.0.0'

pipeline {
    agent any
	
	tools {nodejs "node"}
	
	environment {
		EPHEMERAL_HOST = "${params.EPHEMERAL_HOST}"
		CONTAINER_BACKEND_PATH = "${params.CONTAINER_BACKEND_PATH}"
		API_EPHEMERAL_URL = "http://${EPHEMERAL_HOST}:9998"
		POSTMAN_ENV_URL = "${params.POSTMAN_ENV_URL}"
		POSTMAN_ENV_HOST = "${params.POSTMAN_ENV_HOST}"
		POSTMAN_ENV_PORT = "${params.POSTMAN_ENV_PORT}"
		POSTMAN_ENV_NUM1 = "${params.POSTMAN_ENV_NUM1}"
		POSTMAN_ENV_NUM2 = "${params.POSTMAN_ENV_NUM2}"
	}
	
    stages {
        stage('Prepare backend version') {
			agent {
				docker { image 'maven:3.6.3-jdk-11-slim'}
			}
            steps {
                echo "Getting backend version with maven"
				echo "Before ${backendVersion}"
				sh 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout > backend.txt'
				script {
					backendVersion = "${CONTAINER_BACKEND_PATH}:" + readFile('backend.txt').trim() + "-" + env.BUILD_NUMBER
				}
				echo "After ${backendVersion}"
            }
        }
        stage('Setup compose enviroment') {
            steps {
                echo "Building backend image ${backendVersion}"
				sh "docker build -t ${backendVersion} ."
				echo "Generate docker-compose file"				
				sh "sed -i 's@{{BACKEND_DOCKER_IMAGE}}@${backendVersion}@g' docker-compose.dist"
				sh 'cat docker-compose.dist'
				sh "docker-compose -f docker-compose.dist up -d"
				sh "sleep 20"
				sh "docker-compose -f docker-compose.dist ps"
            }
        }
		stage('Setup Postman') {
			steps {
				echo "Test Postman newman"
				sh 'newman run ${POSTMAN_ENV_URL} --env-var "Host=${POSTMAN_ENV_HOST}" --env-var "Port=${POSTMAN_ENV_PORT}" --env-var "num1=${POSTMAN_ENV_NUM1}" --env-var "num2=${POSTMAN_ENV_NUM2}"'
			}
		}		
    }
	
	post {
	
		always {
			echo "Down ephemeral environment..."
			sh "docker-compose -f docker-compose.dist down"
			sh "docker rmi -f ${backendVersion}"
		}
		
		success {
			echo "success"
		}
		
		unstable {
			echo "unstable"
		}
		
		failure {
			echo "failure"
		}
		
		changed {
			echo "changed"
		}
	}
}