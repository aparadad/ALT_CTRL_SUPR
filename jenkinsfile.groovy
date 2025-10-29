// Jenkinsfile
pipeline {
    // Agente: Se ejecutará en cualquier agente disponible (servidor Jenkins con Docker)
    agent any 

    environment {
        // Constantes basadas en tu proyecto:
        DOCKER_IMAGE = 'mi_aplicacion_go' // Nombre de la imagen que usaste [cite: 25, 27]
        DOCKER_TAG = 'latest'
        GO_MAIN_APP = 'main' // Nombre del binario Go construido
        GO_VERSION = 'golang:1.25' // Versión base Go para el Dockerfile 
    }

    stages {
        stage('1. Limpiar Entorno de Prueba') {
            steps {
                echo 'Deteniendo y eliminando contenedores de prueba antiguos...'
                // Detiene y elimina contenedores antiguos para asegurar que el puerto 8080 esté libre
                sh "docker stop \$(docker ps -a -q --filter name=${env.DOCKER_IMAGE}_run) || true"
                sh "docker rm \$(docker ps -a -q --filter name=${env.DOCKER_IMAGE}_run) || true"
            }
        }

        stage('2. Construir Imagen Docker') {
            steps {
                echo 'Construyendo imagen Docker usando Dockerfile...'
                // Utiliza el Dockerfile de tu proyecto [cite: 22, 23]
                // El punto '.' indica que el contexto de construcción es la raíz del repositorio.
                sh "docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ."
            }
        }
        
        stage('3. Ejecutar Prueba Local (Verificación)') {
            steps {
                echo "Lanzando contenedor en puerto ${params.PORT_NUMBER ?: '8080'} para verificación..."
                // Lanza el contenedor y mapea el puerto 8080, usando el nombre del contenedor para fácil limpieza.
                sh "docker run -d --name ${env.DOCKER_IMAGE}_run -p 8080:8080 ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}"
            }
        }
        
        // El pipeline finalizaría con un paso de despliegue a producción o preproducción
    }
}