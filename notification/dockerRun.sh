export DOCKER_HOST=192.168.1.205
docker stop notification-service
docker image rm notification-service:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t notification-service:0.0.1 .
docker run --rm -d --name notification-service -p 8803:8803 notification-service:0.0.1