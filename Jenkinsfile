// Jenkinsfile
pipeline {
    agent any 

options {
        // Aumenta el intervalo de chequeo a 1 hora (3600 segundos) para evitar que
        // Jenkins mate tareas de shell muy rápidas o lentas
        skipDefaultCheckout true // Opcional, pero recomendado en el entorno de Docker
        timeout(time: 1, unit: 'HOURS')
        wrapper {
            properties {
                'org.jenkinsci.plugins.durabletask.BourneShellScript.HEARTBEAT_CHECK_INTERVAL' = 3600
            }
        }
    }
	
    environment {
        // Variables basadas en tu Producto 1 y Dockerfile
        DOCKER_IMAGE = 'mi_aplicacion_go' 
        DOCKER_TAG = 'latest'
        CONTAINER_RUN_NAME = "test-go-app" // Nombre fijo para fácil limpieza
    }

    stages {
        stage('1. Limpiar Entorno de Prueba') {
            steps {
                echo 'Deteniendo y eliminando contenedores de prueba antiguos...'
                // Detiene y elimina el contenedor de verificación para asegurar que el puerto 8080 esté libre.
                sh "docker stop \$(docker ps -a -q --filter name=${env.CONTAINER_RUN_NAME}) || true"
                sh "docker rm \$(docker ps -a -q --filter name=${env.CONTAINER_RUN_NAME}) || true"
            }
        }

        stage('2. Construir Imagen Docker') {
            steps {
                echo 'Construyendo imagen Docker usando Dockerfile...'
                // Construye la imagen usando el Dockerfile en la raíz.
                sh "docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ."
            }
        }
        
		stage('3. Ejecutar Prueba Local (Verificación)') {
            steps {
                echo 'Lanzando contenedor local en el puerto 8081 del servidor para verificación...'
                // Mapeamos el puerto 8081 del HOST al 8080 interno del contenedor
                sh "docker run -d --name ${env.CONTAINER_RUN_NAME} -p 8081:8080 ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}"
                echo "¡APLICACIÓN GO ACCESIBLE EN http://[IP_Servidor]:8081!"
            }
        }
        
    }
    
    // ----------------------------------------------------
    // POST-BUILD: Limpieza del Contenedor de Prueba
    // ----------------------------------------------------
    post {
        failure {
            echo '¡El pipeline falló! Manteniendo el contenedor para diagnóstico.'
        }
        success {
            echo 'Prueba local completada. Deteniendo contenedor de verificación.'
            // Detiene y elimina el contenedor de verificación para liberar el puerto 8080 para futuras construcciones.
            sh "docker stop ${env.CONTAINER_RUN_NAME}"
            sh "docker rm ${env.CONTAINER_RUN_NAME}"
        }
    }
}

