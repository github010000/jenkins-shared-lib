def call() {
    pipeline {
        agent none
        stages {
            stage ('Example') {
                steps {
                    script {
                        log.info 'Starting'
                        log.warning 'Nothing to do!'
                    }
                }
            }
        }
    }
}