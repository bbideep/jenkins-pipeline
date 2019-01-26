import hudson.plugins.git.*;
import jenkins.model.Jenkins;

def user = new File("/run/secrets/scm-repo-user").text.trim()

def getCredentialsId = { username ->
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials.class,
        jenkins.model.Jenkins.instance
    )

    def c = creds.findResult { it.username == username ? it : null }

    if ( c ) {

        def systemCredentialsProvider = jenkins.model.Jenkins.instance.getExtensionList(
            'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
            ).first()

        def id = systemCredentialsProvider.credentials.first().id
        return id
    } else {
      println "could not find credential for ${username}"
    }
}

def GIT_REPO_HTTPS_URL = System.env.GIT_REPO_HTTPS_URL
def GIT_BRANCH = System.env.GIT_BRANCH
def scm = new GitSCM(GIT_REPO_HTTPS_URL)
scm.branches = [new BranchSpec("*/"+GIT_BRANCH)];
scm.userRemoteConfigs = [new UserRemoteConfig(GIT_REPO_HTTPS_URL, null, null, getCredentialsId(user))]

def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")

def parent = Jenkins.instance
def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Container_Deployment_Demo")
job.definition = flowDefinition

parent.reload()
