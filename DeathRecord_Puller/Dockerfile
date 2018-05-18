FROM tomcat:latest
MAINTAINER Mike Riley "michael.riley@gtri.gatech.edu"

RUN apt-get update -y && apt-get upgrade -y

RUN apt-get install -y \
      git \
      postgresql \
      openjdk-8-jdk \
      maven
	  
ADD . /usr/src/deathrecord_src
RUN mvn clean install -DskipTests -f /usr/src/deathrecord_src/ecr_javalib
RUN mvn clean install -DskipTests -f /usr/src/deathrecord_src/

RUN wget https://certs.godaddy.com/repository/gdig2.crt.pem
RUN keytool -import -alias gdig2_cert_pem -keystore /etc/ssl/certs/java/cacerts -file gdig2.crt.pem -storepass changeit -noprompt
RUN cp /usr/src/deathrecord_src/target/DeathRecord-0.0.1-SNAPSHOT.war $CATALINA_HOME/webapps/
