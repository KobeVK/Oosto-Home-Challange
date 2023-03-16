# Oosto-Home-Challange


This Deployment pipeline is deploying a simple site behind an nginx proxy that knows to return a string from a specific url path
The pipeline can get deploy the infrastructure in two ways, distinguished by a flag:
1. REBUILD-CLUSTER=true

    terraform will create a remote server on aws with the AWS modules
    1. Will install all prerequisites
    2. will setup k3s cluster
    3. will setup helm + the nginx ingrees to take requests from the world (see attached architecture)

2. REBUILD-CLUSTER=false
    The deployment will made to an existing envoirnmet that was pre-configured (with public-private-keys-access)

![Oosto web app CI/CD architecture](architecture.png)

# How to run
This repo is connected to Jenkins via a webhook, evey push-commit will trigger a pipeline.
For future-work - I will make it a multi-branch pipeline, so it will deploy feature-branches to one enviorment, and main to "production enviorment" more scalable and reliable.

in Jenkins - start building the 'devpoy_env'
you will be promted with entering the following variables:
1. 'deploy_new_env'
   a boolean param (True | False)
   when set to false = deploy the webapp on an existing ec2 instance, pre-configured
   when set to true = create a new ec2 instance and deploy the webapp using ansible (not fully supported yet)
2. 'stagingIP'
   The IP of the existing enviornemt to deploy the app when deploy_new_env is set to False
3. 'string_to_print'
   The purpose of the webapp is to listening on an http port and return a string when called
   In order to test and verify the CI/CD flow, one can grant the string, so the test will be able to compare what entered and when returned, and notify via email in case the flow is broken.
4. 'mailTo'
   an email, or lists of emails to sent to failure notifications to.

The implementation of service-pod-ingress is very effective:

The Service acts as a load balancer.

The pods contain the web app code and any necessary dependencies. They can be scaled horizontally to handle more traffic when needed.

The Ingress is used to expose the web app to external traffic by routing requests to the appropriate Service.

By using this architecture, i found that concers can be separated concerns and decouple the web app from the infrastructure, making it easier to manag.
in addition, the Service-Pod-Ingress architecture can also provide fault tolerance - as the Service can automatically route traffic to healthy pods in case of a failure.

in addition, I implemnted the solution over a single-node k3s becuase of it's lightweight and Low resource utilization characteristics. 
Overall, running K3s in a single node can simplify the deployment and management of Kubernetes, reduce resource utilization, and improve performance.