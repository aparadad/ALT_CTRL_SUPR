// Jenkinsfile (Pipeline de Integración Básica)
pipeline {
    agent any

    stages {
        stage('1. Clonar y Copiar Ficheros') {
            steps {
                // La variable BRANCH_NAME es detectada automáticamente por el Pipeline Multibranch.
                echo "Pipeline activado por webhook. Rama detectada: ${env.BRANCH_NAME}"
                
                // ********* Lógica Condicional para Distinguir Ramas *********
                script {
                    if (env.BRANCH_NAME == 'main') {
                        echo 'Ejecutando proceso de copia para el entorno de PRODUCCIÓN (main).'
                        // Aquí iría tu comando de copia/despliegue específico para main.
                        sh 'ls -l' 
                    } else if (env.BRANCH_NAME == 'preprod') {
                        echo 'Ejecutando proceso de copia para el entorno de PREPRODUCCIÓN (preprod).'
                        // Aquí iría tu comando de copia/despliegue específico para preprod.
                        sh 'ls -l'
                    } else {
                        // Opcional: Ignorar otras ramas
                        echo "Rama ${env.BRANCH_NAME} detectada. Solo se procesan 'main' y 'preprod'."
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo "Proceso de CI básico finalizado para ${env.BRANCH_NAME}."
        }
    }
}
