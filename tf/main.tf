provider "aws" {
  region = "us-west-2" # can change to be acquired from aws-cli #TODO: feature work
}

resource "aws_security_group" "k3s_security_group" {
  name_prefix = "k3s_sg_"
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 6443
    to_port = 6443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "null_resource" "k3s_installation" {
  connection {
    host        = var.instance_ip
    type        = "ssh"
    user        = "ubuntu"
    private_key = file(var.ssh_private_key_path)
  }

  provisioner "remote-exec" {
    inline = [
      "curl -sfL https://get.k3s.io | INSTALL_K3S_VERSION=v1.21.2+k3s1 sh -",
    ]
  }
}

variable "instance_ip" {
  description = "Public IP of the existing EC2 instance"
}

variable "ssh_private_key_path" {
  description = "Path to the SSH private key used to connect to the existing EC2 instance"
}

output "k3s_server_address" {
  value = var.instance_ip
}
