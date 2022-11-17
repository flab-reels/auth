FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
ENV APP_NAME Auth
# install tini
COPY $JAR_FILE /bin
#COPY ${JAR_FILE} app.jar/

#ENTRYPOINT ["java","-jar","/app.jar"]
#11Ver change


VOLUME /bin
EXPOSE 8080
# use tini to avoid zombie processes and allow better shutdown
ENTRYPOINT ["/sbin/tini", "-e", "143", "-g", " - "]
CMD ["java", "-jar", "/bin/${JAR_FILE}.jar"]