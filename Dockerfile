#Stage 1:
FROM maven:4.0.0-rc-5-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY /src ./src

RUN mvn clean package -DskipTests

#Stage 2:
#Create image to run jar
FROM amazoncorretto:21.0.4
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Commant to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
