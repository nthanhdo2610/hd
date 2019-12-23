set DOCKER_HOST=192.168.2.5
docker stop filehandler
docker image rm filehandler:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t filehandler:0.0.1 .
docker run --rm -d --name filehandler -p 8802:8802 filehandler:0.0.1