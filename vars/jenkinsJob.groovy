def call(){
    node {
        stage('Checkout') {
            checkout scm
        }

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
        withSonarQubeEnv(credentialsId: 'SonarQube_Local') {
          bat "mvn sonar:sonar -Dsonar.projectKey=${env.JOB_NAME}"  
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
