def call(){
    node {
        stage('Checkout') {
            checkout scm
        }

        sonarQubeScan()

        // Execute different stages depending on the job
        if(env.JOB_NAME.contains("deploy")){
            packageArtifact()
        } else if(env.JOB_NAME.contains("test")) {
            buildAndTest()
        }
    }
}

def sonarQubeScan(){
    stage("Sonar Scan") {
        withSonarQubeEnv('SonarQube_Local') {
          bat "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar -Dsonar.projectKey=${env.JOB_NAME}"
        }

        def qg = waitForQualityGate()
        if (qg.status != 'OK') {
            error "Pipeline aborted due to quality gate failure: ${qg.status}"
        }
    }
}

def packageArtifact(){
    stage("Package artifact") {
        bat "mvn package"
    }
}

def buildAndTest(){
    stage("Backend tests"){
        bat "mvn test"
    }
}
