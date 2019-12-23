export DOCKER_HOST=192.168.75.5
docker stop documentation-api
docker image rm documentation-api:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t documentation-api:0.0.1 .
docker run --rm -d --name documentation-api -p 9093:9093 documentation-api:0.0.1