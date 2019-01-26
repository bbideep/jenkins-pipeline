import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

def username = new File("/run/secrets/scm-repo-user").text.trim()
def password = new File("/run/secrets/scm-repo-pwd").text.trim()

def aws_access_key = new File("/run/secrets/aws-access-key").text.trim()
def aws_secret_key = new File("/run/secrets/aws-secret-key").text.trim()

def docker_reg_user = new File("/run/secrets/docker-reg-user").text.trim()
def docker_reg_pwd = new File("/run/secrets/docker-reg-pwd").text.trim()

def k8s_user = new File("/run/secrets/k8s-user").text.trim()
def k8s_pwd = new File("/run/secrets/k8s-pwd").text.trim()


//Credentials scm_creds = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,java.util.UUID.randomUUID().toString(), "scm-credentials", username, password)
Credentials scm_creds = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "scm-credentials", "scm-credentials", username, password)
Credentials aws_creds = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "aws-credentials", "", aws_access_key, aws_secret_key)
Credentials docker_reg_creds = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "ecr-credentials", "docker-reg-creds", docker_reg_user, docker_reg_pwd)
Credentials k8s_creds = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "k8s-credentials", "k8s-credentials", k8s_user, k8s_pwd)

SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), scm_creds)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), aws_creds)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), docker_reg_creds)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), k8s_creds)
