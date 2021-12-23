FROM openjdk:8
EXPOSE 8080
ADD build/libs/moviex-0.0.1-SNAPSHOT.jar moviex-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "moviex-0.0.1-SNAPSHOT.jar"]