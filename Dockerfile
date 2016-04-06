FROM tomcat:8-jre8

MAINTAINER "Joway Wong"

RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]

ADD ./target/UploadFiles.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
