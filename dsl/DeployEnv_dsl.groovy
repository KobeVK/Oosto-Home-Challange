jobName = "devpoy_env"

def dslJob = pipelineJob(jobName)

dslJob.with {
    parameters {
        booleanParam(name: 'deploy_new_env', defaultValue: false, description: 'True/False')
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
