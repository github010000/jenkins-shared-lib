def call(Map config = [:]) {
    pipeline {
        agent any

        tools {
            maven 'M3'
        }

        options {
            disableConcurrentBuilds()
            skipDefaultCheckout(true)
            buildDiscarder(logRotator(numToKeepStr: '10'))
        }

        stages {
            stage('Check build permission') {
                steps {
                    // send build started notifications
                    sendNotifications 'STARTED',"${config.mm_channel}"
                    script {
                        sh "env | sort"
                    }
                }
            }
            stage('Checkout source') {
                steps {
                    cleanWs()
                    checkout scm
                }
            }
            stage('Set Variable') {
                steps {
                    script {
                        sh """
                           echo  Set Variable
                           mvn -version
                        """
                    }
                }
            }
            stage('Build Jar') {
                steps {
                    // build
                    sh "mvn -Dmaven.test.skip=true -e clean package"
                }
            }
            stage('Test') {
                steps {
                    script {
                        // sh "mvn -P all-tests -Dmaven.test.failure.ignore=true -Dspring.profiles.active=local -B verify jacoco:prepare-agent jacoco:report --quiet"
                        // systemCommand = load('/var/lib/jenkins/scriptler/scripts/systemCommand.groovy')
                        //systemCommand.system("mvn -version")
                        sh "echo  Test"

                    }
                }
            }
            stage('SonarQube analysis') {
                steps {
                    sh "echo  SonarQube analysis"
                }
            }
            stage('SonarQube Quality Gate') {
                steps {
                    sh "echo  SonarQube Quality Gate"
                }
            }
            stage('Deployment') {
                steps {
                    sh "echo  Deployment"
                }
            }
            stage('Health Check') {
                steps {
                    sh "echo  Helath Check"
                }
            }
        }
        post {
            always {
                cleanWs(cleanWhenNotBuilt: true, deleteDirs: true, disableDeferredWipeout: false, notFailBuild: true)
                sendNotifications currentBuild.result,"${config.mm_channel}"
            }
        }
    }
}