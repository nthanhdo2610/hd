set DOCKER_HOST=192.168.1.205
docker stop customer
docker image rm customer:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t customer:0.0.1 .
docker run --rm -d --name customer -p 8800:8800 customer:0.0.1