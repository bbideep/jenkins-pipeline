FROM jenkins/jenkins:lts-alpine

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

COPY files/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY scripts/*.groovy /usr/share/jenkins/ref/init.groovy.d/

USER root
RUN chown jenkins /usr/share/jenkins/ref/init.groovy.d/*.groovy \
        && echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state

#RUN ls -la /usr/share/jenkins/ref/ && ls -la /usr/share/jenkins/ref/plugins/ && ls -la /usr/share/jenkins/ref/init.groovy.d/

USER jenkins