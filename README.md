# Oosto-Home-Challange
pipelines:
1. terraform will create server with ansible, and will run a playbook, that buiulds k8s cluster single node
    ansible will deploy nginx webserver eith helm chart
    nginx should display custom massage which will be passed throght ansible variable
    Example: “Hello World! I’m a Senior DevOps Engineer candidate @ Oosto!”

Notes:
flag to redeploy a new cluster - if not, just perform ansible playbooks
