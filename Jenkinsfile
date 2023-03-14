#!groovy

def ENVIRONMENT = ""
def buildNumber = env.BUILD_NUMBER as int
def mailTo = 'skvaknin@gmail.com'
def stagingIP = "15.237.160.86"

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

		stage('Verify hosts are up') {
			when {
                expression {
                    params.REBUILD_CLUSTER != "1"
                }
			steps {
				script {
					properties([
						ansible all -m ping
					])
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

		// stage('test') {
		// 	steps {
		// 		script{
		// 			sh """
        //             	kubectl describe service apple-service | grep Endpoints | awk '{print $2}'
        //         	"""
		// 		}
		// 	}
		// 	post{
		// 	    failure {
		// 		    script{
		// 			    sendEmail(mailTo)
		// 		    }
		// 	    }
		//     }	
		// }

		// stage('Release') {
		// 	steps {
        //         withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
		// 			script{
		// 				sh """
		// 					sed -i 's/hosts: all/hosts: ${env.IP}/' release_docker_playbook.yml > /dev/null 1>&2
		// 				"""
		// 			}
		// 			ansiblePlaybook(
		// 				playbook: 'release_docker_playbook.yml',
		// 				extraVars: [
		// 					usr: "${USERNAME}",
		// 					pass: "${PASSWORD}",
		// 					buildNumber: "${buildNumber}",
		// 					envioronment: "${env.ENVIRONMENT}"
		// 				]
		// 			)
		// 		}
		// 	}
		// }
	}
}

// def deployNewEnv() {
// 	def buildNumber = env.BUILD_NUMBER
// 	sh """
// 		echo "Starting Terraform init"
// 		terraform init /path/to/your/terraform/file.tf
// 		// terraform plan -out myplan -var="environment=${env.ENVIRONMENT}" -var="id=${buildNumber}"  
// 		// terraform apply -auto-approve -var="environment=${env.ENVIRONMENT}" -var="id=${buildNumber}"
// 	"""
// }

def deployExistingEnv() {
	sh """
		echo "Starting Deploying app on "Staging": existing aws instance ""
		echo "This will install k3s cluster, and deploy a webpage behind an nginx on pod, and display string as a massage"
		ansible-playbook ansible/playbook.yaml -e "app_string=kobkob"
	"""
}

// def destroyENV() {
// 	def buildNumber = env.BUILD_NUMBER
// 	sh """
// 		sleep 600
// 		echo "Starting Terraform destroy"
// 		terraform destroy -auto-approve -var="environment=${env.ENVIRONMENT}" -var="id=${buildNumber}"
// 	"""
// }

// def sendEmail(mailTo) {
//     println "send mail to recipients - " + mailTo
//     def strSubject = "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
//     def strBody = """<p>FAILED: Job <b>'${env.JOB_NAME} [${env.BUILD_NUMBER}]'</b>:</p>
//         <p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>"""
//     emailext body: strBody, subject: strSubject, to: mailTo, mimeType: "text/html"
// }