provider "gitlab" {
  token    = "AwXXXXXXXX"
  base_url = "https://gitrepo.georgebrown.ca/api/v4/"
  insecure = true
}

terraform {
  backend "s3" {
    bucket = "c4po-tfxXX"
    key    = "gbc/gitlab.tfstate"
    region = "us-east-1"
access_key ="AKIAXXXXXXXX"

secret_key ="H7taL2SXXXXXXXXXX"
  }
}
