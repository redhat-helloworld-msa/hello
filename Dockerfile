FROM registry.access.redhat.com/jboss-eap-7/eap71-openshift 

ENV JAVA_OPTS="-Xms64m -Xmx512m -Djboss.modules.system.pkgs=org.jboss.logmanager"
ENV ZIPKIN_SERVER_URL http://zipkin-query:9411

RUN rm /opt/eap/standalone/deployments/activemq-rar.rar*

ADD deployments/ROOT.war /opt/eap/standalone/deployments/
