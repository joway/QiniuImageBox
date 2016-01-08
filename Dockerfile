FROM tomcat:8-jre8

MAINTAINER "Joway Wong"

ADD target/UploadFiles.war /usr/local/tomcat/webapps/
