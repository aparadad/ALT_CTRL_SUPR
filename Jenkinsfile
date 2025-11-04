// Jenkinsfile (Integración CI/CD con K3s)
pipeline {
    agent any

    environment {
        // Variables para la imagen y el versionado
        DOCKER_IMAGE = 'mi_aplicacion_go'
        APP_VERSION = "${env.BUILD_NUMBER}" // Usamos el ID de la construcción para el versionado
        
        // El archivo de manifiesto se asume en la raíz para simplicidad.
        DEPLOYMENT_FILE = 'deployment.yaml' 
    }

    stages {
        stage('1. Clonar y Copiar Ficheros') {
            steps {
                echo "Pipeline activado. Rama detectada: ${env.BRANCH_NAME}"
                // El checkout del código se realiza automáticamente al inicio
            }
        }

        stage('2. Construir y Etiquetar Imagen') {
            steps {
                echo "Construyendo imagen Docker: ${env.DOCKER_IMAGE}:${env.APP_VERSION}"
                // Construye la imagen con un tag único basado en el número de build
                sh "docker build -t ${env.DOCKER_IMAGE}:${env.APP_VERSION} ."
            }
        }
        
        stage('3. Despliegue Condicional en K3s') {
            steps {
                // Lógica Condicional para Distinguir Ramas (Entornos)
                script {
                    if (env.BRANCH_NAME == 'main' || env.BRANCH_NAME == 'preprod') {
                        
                        echo "Iniciando despliegue en entorno: ${env.BRANCH_NAME}..."
                        
                        // 1. Cargar la imagen en el registro interno de K3s (containerd)
                        echo 'Cargando imagen en el runtime de K3s...'
                        sh "docker save ${env.DOCKER_IMAGE}:${env.APP_VERSION} | sudo k3s ctr images import -"
                        
                        // 2. Aplicar el manifiesto de Kubernetes
                        echo 'Aplicando manifiesto para alta disponibilidad (replicas: 2)...'
                        sh "kubectl apply -f ${env.DEPLOYMENT_FILE}" 
                        
                        // 3. Verificación de Pods (espera activa hasta que los pods estén listos)
                        sh "kubectl rollout status deployment/go-web-deployment --timeout=120s"
                        
                        // Añadir un paso de aprobación manual solo para la rama MAIN
                        if (env.BRANCH_NAME == 'main') {
                            input message: 'Verificación de PREPROD exitosa. ¿CONFIRMAS el despliegue a PRODUCCIÓN (MAIN)?', ok: 'Proceder'
                            // Aquí se podría añadir una lógica de despliegue distinta para Producción si fuera necesario
                        }
                    } else {
                        echo "Rama ${env.BRANCH_NAME} detectada. No es un entorno de despliegue final."
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "Proceso finalizado. El despliegue de ${env.BRANCH_NAME} está activo en K3s."
        }
    }
}
