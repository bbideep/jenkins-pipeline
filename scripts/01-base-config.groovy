import jenkins.model.*

//This env variable is set in the docker compose file through shell env variable.
url = System.env.JENKINS_UI_URL
urlConfig = JenkinsLocationConfiguration.get()
urlConfig.setUrl(url)
urlConfig.save()
