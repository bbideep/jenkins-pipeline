## Set-up Instructions


1. Configure the below environment variables.   
	 ```$ export JENKINS_URL=http://<JENKINS HOST>:8081/jenkins```   
	 Port number used here is 8081. It can be changed here according to the configuration in the pipeline-compose.yml file.

2. Here, we are using docker secrets to configure the Jenkins Admin username and password. To use docker secrets, we need to initialize docker swarm on the node where Jenkins has to be configured.
   Run ```$ docker swarm init``` to initialize docker swarm.   
   Verify that the node is part of the swarm by ```$ docker node ls```.

3. Run the below commands to populate the secrets. Replace the values as needed.   
   ```$ echo "your jenkins admin username" | docker secret create admin-username -```   
   ```$ echo "your jenkins admin password" | docker secret create admin-password -```   

## Build the docker image

Use the below command to build the docker image and run locally without any image registry. In case you provide a different tag, the pipeline-compose.yml file needs to be updated accordingly.   
```$ docker build -f Dockerfile -t bbideep/jenkins:1.0 .```

## Build the stack

Deploy 'jenkins' stack:  
```$ docker stack deploy -c pipeline-compose.yml jenkins```  

Remove 'jenkins' stack:  
```$ docker stack rm jenkins```  

## Additional commands cheat sheet

Remove docker secrets:  
```$ docker secret rm admin-username```  
```$ docker secret rm admin-password```
