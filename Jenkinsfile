#!groovy

def ENVIRONMENT = ""
def buildNumber = env.BUILD_NUMBER as int
def mailTo = 'skvaknin@gmail.com'
def stagingIP = '13.39.86.252'
def string_to_print = 'Hello World! I am a Senior DevOps Engineer candidate @ Oosto!'


pipeline {
	agent any
	parameters {
		string(name: 'region', defaultValue: 'eu-west-3')
		string(name: 'existing_enviornemt', defaultValue: stagingIP)
		string(name: 'string', defaultValue: string_to_print)
		
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
						deployExistingEnv(string_to_print)
					}
				}
			}
		}

		stage('verify') {
			steps {
				script {
					try {
						test_in_k3s = sh(
							script: """
								ansible-playbook ansible/verification.yaml
							""", returnStdout: true
						).trim()

						if (test_in_k3s != string_to_print) {
							throw new Exception("Strings do not match.")
						}
					}
					catch (Exception ex) {
						echo "**** Deployment Failed, Final string is not correct. ****"
						sh "exit 1"
					}
				}
			}
		}
	}
}


def deployExistingEnv(string) {
	sh """
		echo "Starting Deploying app on "Staging": existing aws instance "
		echo "This will install k3s cluster, and deploy a webpage behind an nginx on pod, and display string as a massage"
		ansible-playbook ansible/playbook.yaml -e "app_string=${string}"
	"""
}