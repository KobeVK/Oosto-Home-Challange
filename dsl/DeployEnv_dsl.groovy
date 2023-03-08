jobName = "devpoy_env"

def dslJob = pipelineJob(jobName)

dslJob.with {
    parameters {
        booleanParam('deploy_new_env', false, '<font size=2 color=blue>Set to True if you wish to create new cluster on EC2, otherwise, app will be deployed on existing envoirnemt(staging) </font>')
    }

    logRotator {
        numToKeep(10)
    }
    definition {
        cpsScm {
            scm {
                git {
                    branch('master')
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
