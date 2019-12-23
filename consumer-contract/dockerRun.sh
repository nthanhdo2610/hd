export DOCKER_HOST=192.168.75.5
docker stop consumer-contract
docker image rm consumer-contract:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t consumer-contract:0.0.1 .
docker run --rm -d --name consumer-contract -p 8807:8807 consumer-contract:0.0.1