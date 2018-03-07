FROM openweb/oracle-tomcat:7-jre8

COPY sources.list /etc/apt/sources.list
RUN apt-get -y update && apt-get install -y graphviz

RUN rm -rf /usr/local/tomcat/webapps/ROOT*

COPY target/image_server.war /usr/local/tomcat/webapps/ROOT.war
