def GITHUB_TOKEN = credentials('github-auth')
def POD_LABEL = 'kaniko'
    node(POD_LABEL) {
    def DOCKER_IMAGE_NAME = "gwrgwr/technova-ms-user:${env.BUILD_ID}"
    def DOCKER_IMAGE_NAME_LATEST = "gwrgwr/technova-ms-user:latest"
        stage('Checkout') {
            checkout scm
        }

        stage('Build with Kaniko') {
            container('kaniko') {
                sh '''#!/busybox/sh
                                /kaniko/executor \
                                  --context `pwd` \
                                  --build-arg GITHUB_TOKEN=$GITHUB_TOKEN \
                                  --dockerfile=./Dockerfile \
                                  --destination ''' + DOCKER_IMAGE_NAME + ''' \
                                  --destination ''' + DOCKER_IMAGE_NAME_LATEST + '''\
                            '''
            }
        }

        stage('Checkout Helm Chart') {
            git url: 'https://github.com/gwrgwr/technova-helm.git', branch: 'master', credentialsId: 'github-auth'
        }

        stage('Deploy to Kubernetes') {
            container('kubectl') {
                withKubeConfig([credentialsId: 'jenkins-token', namespace: 'jenkins', serverUrl: 'https://192.168.49.2:8443']) {
                    sh """
                        helm upgrade --install technova-ms-user ./charts/user/ \
                        --values values.yaml \
                        --values charts/user/values.yaml \
                        --set user.image.tag=${env.BUILD_ID} \
                        --namespace technova \
                        --wait \
                        --atomic
                        """
                }
            }
        }
    }