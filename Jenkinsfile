#!groovy

def ENVIRONMENT = ""
def buildNumber = env.BUILD_NUMBER as int
def mailTo = 'skvaknin@gmail.com'
def stagingIP = "52.47.111.150"

pipeline {
	agent any
	parameters {
		string(name: 'region', defaultValue: 'eu-west-3')
		
	}

	environment {
      branch = "${env.GIT_BRANCH}"
	  GIT_SSH_COMMAND = "ssh -o StrictHostKeyChecking=no"
      REBUILD_CLUSTER = "${params.rebuild_cluster}"
	}

	options {
		timestamps()
	}

	stages {

		stage('starting-fresh') {
            steps {
                script {
                    deleteDir()
					cleanWs()
                    checkout scm
                    if (buildNumber > 1) milestone(buildNumber - 1)
                    milestone(buildNumber)
                }
            }
        }

		stage('Properties Set-up') {
			steps {
				script {
					properties([
						disableConcurrentBuilds()
					])
				}
			}
		}

		stage('verify hosts are reachable') {	
			when {
                expression {
                    params.REBUILD_CLUSTER != "1"
                }
			}
			steps {
				script{
					sh """
						ansible all -m ping
					"""
				}
			}
		}

		stage('build') {
            steps {
				script {
					if ( params.REBUILD_CLUSTER == "1" ){
						deployNewEnv()
					}
					else {
						deployExistingEnv()
					}
				}
			}
		}
	}
}



def deployExistingEnv() {
	sh """
		echo "Starting Deploying app on "Staging": existing aws instance "
		echo "This will install k3s cluster, and deploy a webpage behind an nginx on pod, and display string as a massage"
		ansible-playbook ansible/playbook.yaml -e "app_string=kobkob" -vvv
	"""
}