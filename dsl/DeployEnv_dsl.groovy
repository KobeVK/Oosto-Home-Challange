jobName = "devpoy_env"

def dslJob = pipelineJob(jobName)

dslJob.with {
    parameters {
        booleanParam('deploy_new_env', false, '<font size=2 color=blue>Check this option to install the extension with release versions (true), or install dev version only(false)</font>')
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
