# hello
Hello microservice using Java EE (JAX-RS) on EAP

Build and Deploy hello
-------------------------

1. Make sure you have started the JBoss EAP server
2. Open a command prompt and navigate to the root directory of this microservice.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/hello.war` to the running instance of the server.

Access the application
----------------------

The application will be running at the following URL: <http://localhost:8080/hello/api/hello>

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Deploy the application in Openshift
-----------------------------------

1. Make sure to be connected to the Docker Daemon
2. Execute

		mvn clean package docker:build fabric8:json fabric8:apply -Popenshift


