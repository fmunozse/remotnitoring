#!/usr/bin/env groovy

def DOCKER_IMAGE="fmunozse/smurfhouse"
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
    }

    stage('install tools') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-yarn -DnodeVersion=v8.9.1 -DyarnVersion=v1.3.2"
    }

    stage('yarn install') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn"
    }

    stage('backend tests') {
        try {
            sh "./mvnw test"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
        }
    }

    stage('frontend tests') {
        try {
            sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn -Dfrontend.yarn.arguments=test"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/test-results/karma/TESTS-*.xml'
        }
    }

    stage('packaging') {
        sh "./mvnw verify -Pprod -DskipTests"
        //archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }

/*
    def dockerImage
    stage('build docker') {
        sh "cp -R src/main/docker target/"
        sh "cp target/*.war target/docker/"
        dockerImage = docker.build('fmunozse/remotnitoring', 'target/docker')
    }

    stage('publish docker') {
        docker.withRegistry('https://registry.hub.docker.com', 'fmunozse') {
            dockerImage.push 'latest'
        }
    }
*/    
}
