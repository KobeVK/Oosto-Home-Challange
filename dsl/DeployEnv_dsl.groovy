jobName = "devpoy_env"

def dslJob = pipelineJob(jobName)
def stagingIP = "52.47.111.150"
def string_to_print = "52.47.111.150"

dslJob.with {
    parameters {
        booleanParam('deploy_new_env', false, 'Set to True if you wish to create new cluster on EC2, otherwise, app will be deployed on existing envoirnemt(staging)')
		string(name: 'existing_enviornemt', defaultValue: stagingIP)
		string(name: 'string', defaultValue: string_to_print)
    }

    logRotator {
        numToKeep(10)
    }
    definition {
        cpsScm {
            scm {
                git {
                    branch('main')
                    remote {
                        url('https://github.com/KobeVK/Oosto-Home-Challange.git')
                        credentials('github')
                    }
                    extensions {
                        cloneOptions {
                        shallow true
                        noTags true
                        }
                    }
                }
            }
        scriptPath('Jenkinsfile')
        }
    }
}
