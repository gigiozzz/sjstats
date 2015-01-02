<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Websphere Stats Agent Web</title>
	</head>
	<body>
		<h2>Websphere Stats Agent Web</h2>
		<table>
			<tr>
				<td>Node</td><td>Metrics</td>
			</tr>	
			<c:forEach items="${statsMap}" var="entry">
				<c:forEach items="${entry.value}" var="stat">
					<tr>
						<td>${entry.key}</td><td>${stat.hostname} ${stat.server} ${stat.metric} ${stat.value}</td>
					</tr>	
				</c:forEach>			
			</c:forEach>
		</table>
	</body>
</html>