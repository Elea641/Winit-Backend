FROM amazoncorretto:17
WORKDIR /app
CMD [ "./mvnw", "spring-boot:run" ]