FROM jboss-eap-7-beta/eap70-openshift

ADD deployments/ROOT.war /opt/eap/standalone/deployments/
