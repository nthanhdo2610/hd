set DOCKER_HOST=192.168.1.205
docker stop notification
docker image rm notification:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t notification:0.0.1 .
docker run --rm -d --name notification -p 8803:8803 notification:0.0.1