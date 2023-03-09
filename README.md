# Oosto-Home-Challange

1. terraform will create a remote server with
    will setup k3s cluster


    ansible will deploy nginx webserver eith helm chart
    nginx should display custom massage which will be passed throght ansible variable
    Example: “Hello World! I’m a Senior DevOps Engineer candidate @ Oosto!”

Notes:
flag to redeploy a new cluster - if not, just perform ansible playbooks
I’m going with K3s because it seems to have the largest community, it’s CNCF-certified, and it’s lightweight (~60MB binary).

# steps:
### -- installing Jenkins --
    'sudo apt update'
    'sudo apt install openjdk-11-jre -y'
    'curl -fsSL https://pkg.jenkins.io/debian/jenkins.io.key | sudo tee \
        /usr/share/keyrings/jenkins-keyring.asc > /dev/null
        echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
        https://pkg.jenkins.io/debian binary/ | sudo tee \
        /etc/apt/sources.list.d/jenkins.list > /dev/null'
    'sudo apt update'
    'sudo apt-get install jenkins -y'
    'sudo systemctl enable jenkins'
    'sudo systemctl start jenkins'
### -- install needed plugins -->
    'Terraform'
    'aws'
    'Ansible'
    'jobDSL'
### -- install docker on remote instance -->
    'sudo apt-get update && sudo apt-get install -y docker.io'
    sudo usermod -aG docker ubuntu
### -- install Terrafrom CLI on remote instance -->
    'mkdir k3s-terraform'
    'cd k3s-terraform'