<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Microservices endpoint</title>
</head>
<body style="margin: 0px; padding: 0px; overflow: hidden">
	<div>
		<iframe style="position: absolute;" height="100%" width="100%"
			src="webjars/swagger-ui/2.1.4/?url=<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/api/swagger.json#!/default"></iframe>
	</div>
</body>
</html>