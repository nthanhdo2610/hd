set DOCKER_HOST=192.168.1.205
docker stop news
docker image rm news:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t news:0.0.1 .
docker run --rm -d --name news -p 8804:8804 news:0.0.1