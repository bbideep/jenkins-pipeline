## Set-up Instructions


1. Configure the below environment variables.   
	 - `$ export JENKINS_URL=http://<JENKINS HOST>:8081/jenkins`  
	 Port number used here is 8081. It can be changed here according to the configuration in the pipeline-compose.yml file.
	 - `$ export DOCKER_HOST_PORT=$(stat -c '%g' /var/run/docker.sock)`  
	 This is a required for a hack temporarily. Details are explained below in the 'Build the docker image' section.  
	 - `$ export GIT_REPO_HTTPS_URL="https://github.com/bbideep/springboot-microservices"`  
	 Replace with the appropriate repo URL.  
	 - `$ export GIT_BRANCH="master"`  
	 
	 
2. Here, we are using docker secrets to configure the Jenkins Admin username and password. To use docker secrets, we need to initialize docker swarm on the node where Jenkins has to be configured.
   Run ```$ docker swarm init``` to initialize docker swarm.   
   Verify that the node is part of the swarm by ```$ docker node ls```.

3. Run the below commands to populate the secrets. Replace the values as needed.   
- Required to set up Jenkins admin credentials automatically through groovy setup scripts.  
   ```$ echo "your jenkins admin username" | docker secret create admin-username -```   
   ```$ echo "your jenkins admin password" | docker secret create admin-password -```  
- Required for any AWS CLI/API calls. Ideally, IAM roles should be used.   
   ```$ echo "aws access key" | docker secret create aws-access-key -```   
   ```$ echo "aws secret key" | docker secret create aws-secret-key -```  
- Required for docker registry login.  
   ```$ echo "docker registry username" | docker secret create docker-reg-user -```   
   ```$ echo "docker registry password" | docker secret create docker-reg-pwd -```  
- Required for SCM credentials setup.  
   ```$ echo "scm repo username" | docker secret create scm-repo-user -```   
   ```$ echo "scm repo password" | docker secret create scm-repo-pwd -```  
- Required for Kubernetes secrets setup. 
   ```$ echo "k8s secret name" | docker secret create k8s-user -```   
   ```$ echo "k8s secret token" | docker secret create k8s-pwd -```  

Note: In case AWS ECR is used, `aws ecr get-login --no-include-email` can help retrieve the docker login command with the username and password.

## Build the docker image

Use the below command to build the docker image and run locally without any image registry. In case you provide a different tag, the pipeline-compose.yml file needs to be updated accordingly.   
```$ docker build -f Dockerfile -t jenkins-pipeline:latest .```  

Using ```docker build --build-arg DOCKER_HOST_PORT=<'docker' group port> -f Dockerfile -t jenkins-pipeline:latest .``` as a hack for now as I am using the same docker host for demo purpose.  
***This needs to be fixed to fetch the docker socket group dynamically at container startup.***

## Build the stack

Deploy 'jenkins' stack:  
```$ docker stack deploy -c pipeline-compose.yml jenkins```  

Remove 'jenkins' stack:  
```$ docker stack rm jenkins```  

## Additional commands cheat sheet

Use commands like the ones below to remove docker secrets:  
`$ docker secret rm admin-username`  
`$ docker secret rm admin-password`  

For AWS ECR, below commands can be used to -
- Retrieve and run docker login command:  
`$ DOCKER_LOGIN_CMD=$(aws ecr get-login --no-include-email)`  
`$ eval $DOCKER_LOGIN_CMD`  
- Get the docker login password and use it to create K8S secret:  
`$ DOCKER_PWD=$(echo $DOCKER_LOGIN_CMD | awk -F ' ' {'print $6'})`  
`$ kubectl create secret docker-registry ecr-cred --docker-server=https://<AWS ACCOUNT ID>.dkr.ecr.us-east-1.amazonaws.com --docker-username=AWS --docker-password=$DOCKER_PWD`  
- Retrieve base64 encoded contents of the docker config file from the local system.  
`$ ENCODED_TOKEN=$(cat ~/.docker/config.json | base64 | tr -d '\n')`  
