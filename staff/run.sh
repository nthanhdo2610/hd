docker stop staff-service
docker image rm staff-service:0.0.1
call mvn clean package
docker build --rm -f "Dockerfile" -t staff-service:0.0.1 .
docker run --rm -d --name staff-service -p 8808:8808 staff-service:0.0.1