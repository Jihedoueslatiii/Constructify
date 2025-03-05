FROM openjdk:17
EXPOSE 8088
ADD target/proj1-0.0.1-SNAPSHOT.jar Finance.jar
ENTRYPOINT ["java", "-jar", "Finance.jar"]
