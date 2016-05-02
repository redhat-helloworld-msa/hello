FROM jboss-eap-7-beta/eap70-openshift

ENV JAVA_OPTS="-Xms64m -Xmx512m -Djboss.modules.system.pkgs=org.jboss.logmanager"

RUN rm /opt/eap/standalone/deployments/activemq-rar.rar*

ADD deployments/ROOT.war /opt/eap/standalone/deployments/
