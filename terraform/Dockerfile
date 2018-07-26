FROM  buildpack-deps:latest

RUN apt-get update && apt-get -y install unzip python-dev jq
WORKDIR /tmp

ENV TERRAFORM_VERSION 0.11.3


RUN curl -sSL -O https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip \
&& unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip \
&& rm terraform_${TERRAFORM_VERSION}_linux_amd64.zip \
&& mv terraform /usr/local/bin

WORKDIR /app
