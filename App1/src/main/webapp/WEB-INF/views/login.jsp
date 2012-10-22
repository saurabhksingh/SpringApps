<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Login Page</title>
<link rel="stylesheet" type="text/css" href="resources/css/demo.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var CONTEXT_ROOT = '<%= request.getContextPath() %>'; 
		$("#signup").click(function() {
			document.location.href = CONTEXT_ROOT+'/signup';
			});
	});
</script>
</head>
<body onload='document.f.j_username.focus();'>
 
	<c:if test="${not empty error}">
		<div class="errorblock">
			Login Failed.<br /> Error :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
 
	<form name='f' action="<c:url value='j_spring_security_check' />"
		method='POST'>
 
		<table>
			<tbody>
				<tr>
					<td>User:</td>
					<td><input type='text' name='j_username' value=''>
					</td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='j_password' />
					</td>
				</tr>
				<tr>
      				<td><input type="submit" class="greenButton" value="Login" /></td>
      				<td><input id="signup" type="button" class="greenButton" value="Signup" /></td>
				</tr>
			</tbody>
		</table>
 
	</form>
</body>
</html>