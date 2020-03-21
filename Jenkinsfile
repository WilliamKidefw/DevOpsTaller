backendVersion = '0.0.0'

pipeline {
    agent any
	
	enviroment {
		EPHEMERAL_HOST = "${params.EPHEMERAL_HOST}"
		CONTAINER_BACKEND_PATH = "${params.CONTAINER_BACKEND_PATH}"
		API_EPHEMERAL_URL = "http://${EPHEMERAL_HOST}:9998"
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
				sh "docker buid -t ${backendVersion} ."
				echo "Generate docker-compose file"
				sh "sed -i 's@{{BACKEND_DOCKER_IMAGE}}@${backendVersion}@g' docker-compose.dist"
				sh 'cat docker-compose.dist'
				sh "docker-compose -f docker-compose.dist up -d"
				sh "sleep 5"
				sh "docker-compose -f docker-compose.dist ps"
            }
        }        
    }
	
	post {
	
		always {
			echo "Down ephemeral enviroment..."
			sh "docker-compose -f docker-compose.dist down"
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