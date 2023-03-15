jobName = "devpoy_env"

def dslJob = pipelineJob(jobName)
def stagingIP = "15.188.54.246"

dslJob.with {
    parameters {
        booleanParam('deploy_new_env', false, 'Set to True if you wish to create new cluster on EC2, otherwise, app will be deployed on existing envoirnemt(staging)')
        stringParam('stagingIP', '35.180.111.159', "<font size=2 color=blue>choos on which IP to deploy your app</font>")
        stringParam('string_to_print', 'Hello World! I am a Senior DevOps Engineer candidate @ Oosto!')
        stringParam('mailTo', 'skvaknin@gmail.com')
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
