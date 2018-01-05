#!/usr/bin/env groovy

def DOCKER_IMAGE="fmunozse/remotnitoring"
def GIT_PROJECT="github.com/fmunozse/remotnitoring.git"
def GIT_USER_EMAIL="ci.fmunoze@gmail.com"
def GIT_USER_NAME="ci-fmunozse"
def credentialsId_git = "GIT_USERPWD"
def credentialsId_docker = "DOCKER_USERPWD"

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
        sh "git checkout ${workspace}/pom.xml"
    }

    //versions
    sh 'git rev-parse HEAD > GIT_COMMIT'
    def commitNumber = readFile('GIT_COMMIT').trim()
    def pomv = version();
    def buildVersion = "${pomv}_${env.BUILD_ID}"
    def dockerTag = "${DOCKER_IMAGE}:${buildVersion}"

    stage ('Set Version') {
        echo "Running ${buildVersion} - CommitNumber: ${commitNumber} on ${workspace}. dockerTag: ${dockerTag}. BranchName: ${env.BRANCH_NAME}"
        sh "./mvnw -B versions:set -DgenerateBackupPoms=false -DnewVersion=${buildVersion}"
    }



    stage('install tools') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-yarn -DnodeVersion=v8.9.1 -DyarnVersion=v1.3.2"
    }

    stage('yarn install') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn"
    }


    // stage('backend tests') {
    //     try {
    //         sh "./mvnw test"
    //     } catch(err) {
    //         // throw err
    //     } finally {
    //         junit '**/target/surefire-reports/TEST-*.xml'
    //     }
    // }

    // stage('frontend tests') {
    //     try {
    //         sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn -Dfrontend.yarn.arguments=test"
    //     } catch(err) {
    //         // throw err
    //     } finally {
    //         junit '**/target/test-results/karma/TESTS-*.xml'
    //     }
    // }


    stage('packaging') {
        sh "./mvnw verify -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }

    stage ('versioning and docker?') {
        timeout(time: 1, unit: 'HOURS') {
          input 'Generate docker version and push?'
        }
    }

    stage ('Set GitHub Version') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                      credentialsId: credentialsId_git,
                      usernameVariable: 'GIT_USERNAME',
                      passwordVariable: 'GIT_PASSWORD']]) {

            sh "git config user.email '${GIT_USER_EMAIL}'"
            sh "git config user.name '${GIT_USER_NAME}'"
            sh "git tag -a ${buildVersion} -m 'Raise version ${buildVersion}'"
            sh "git push https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${GIT_PROJECT} --tags"

        }
    }

    stage('creating docker') {
    		echo "aaa"
        sh "./mvnw -Pprod dockerfile:build"

        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credentialsId_docker, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh "docker login --username $USERNAME --password $PASSWORD"
        }

        echo "pushing to ${DOCKER_IMAGE}:latest"
        sh "docker push ${DOCKER_IMAGE}:latest"

        sh "docker tag ${DOCKER_IMAGE}:latest ${dockerTag}"

        echo "pushing to ${dockerTag}"
        sh "docker push ${dockerTag}"

    }

}

def version() {
    String path = pwd();
    def matcher = readFile("${path}/pom.xml") =~ '<version>(.+)</version>'
    return matcher ? matcher[0][1] : null
    
	//def project = new XmlSlurper().parseText(readFile('pom.xml'))
	//def pomv = project.version.text()
	//return pomv    
}

