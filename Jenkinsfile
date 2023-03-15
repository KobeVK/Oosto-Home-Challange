#!groovy

def buildNumber = env.BUILD_NUMBER as int
def mailTo = "${params.mailTo}"
def string_to_print = "${params.string_to_print}"
def stagingIP = "${params.stagingIP}"

pipeline {
	agent any

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
			post{
			    failure {
				    script{
					    sendEmail(mailTo)
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
		ansible-playbook ansible/playbook.yaml -i ${stagingIP} -e "app_string=${string}"
	"""
}

def sendEmail(mailTo) {
    println "send mail to recipients - " + mailTo
    def strSubject = "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def strBody = """<p>FAILED: Job <b>'${env.JOB_NAME} [${env.BUILD_NUMBER}]'</b>:</p>
        <p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>"""
    emailext body: strBody, subject: strSubject, to: mailTo, mimeType: "text/html"
}