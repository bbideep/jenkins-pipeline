FROM jenkins/jenkins:lts-alpine

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ARG DOCKER_HOST_PORT

USER root

#Install Ansible, AWS CLI
RUN apk update

RUN apk add --no-cache --update \
    python \
    python-dev \
    py-pip \
    build-base \
    --virtual build-dependencies \
    libffi-dev \
    openrc \
    docker \
    shadow

RUN pip install --upgrade pip \
#&& pip install ansible \
&& pip install awscli \
&& rc-update add docker

# Install Terraform
RUN curl -O https://releases.hashicorp.com/terraform/0.11.3/terraform_0.11.3_linux_amd64.zip \
&& unzip terraform_0.11.3_linux_amd64.zip \
&& mv terraform /usr/sbin/ \
&& rm -f terraform_0.11.3_linux_amd64.zip

# Install Packer
RUN curl -O https://releases.hashicorp.com/packer/1.2.1/packer_1.2.1_linux_amd64.zip \
&& unzip packer_1.2.1_linux_amd64.zip \
&& mv packer /usr/sbin/ \
&& rm -f packer_1.2.1_linux_amd64.zip

# Install Gradle
ENV GRADLE_VERION=4.10.3
RUN curl -k -L https://services.gradle.org/distributions/gradle-$GRADLE_VERION-all.zip -o gradle-$GRADLE_VERION-all.zip \
&& mkdir -p /opt/gradle \
&& unzip -q -d /opt/gradle gradle-$GRADLE_VERION-all.zip


COPY files/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY scripts/*.groovy /usr/share/jenkins/ref/init.groovy.d/

RUN chown jenkins /usr/share/jenkins/ref/init.groovy.d/*.groovy \
        && echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state

RUN usermod -a -G docker jenkins \
&& groupmod -g $DOCKER_HOST_PORT docker

USER jenkins
ENV PATH=$PATH:/opt/apache-maven-3.5.2/bin:/opt/gradle/gradle-$GRADLE_VERION/bin
