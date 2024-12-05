FROM openjdk:21
WORKDIR /app

COPY target/CoREST-DEVEL.war /app/CoREST-DEVEL.war
EXPOSE 30384
ENTRYPOINT  ["java", "-jar", "CoREST-DEVEL.war"]
