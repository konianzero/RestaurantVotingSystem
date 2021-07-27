FROM tomcat:jdk16-openjdk

RUN rm -fr /usr/local/tomcat/webapps/*
COPY target/voting.war /usr/local/tomcat/webapps/voting.war

EXPOSE 8080