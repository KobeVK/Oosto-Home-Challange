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
            commonPipelineScm(context, '${PIPELINE_BRANCH}', build_url)
                scriptPath('Jenkinsfile')
        }
    }
}
