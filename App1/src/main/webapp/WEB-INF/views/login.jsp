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
		$("#fb_login").click(function() {
			//alert("facebook login");
			document.location.href = CONTEXT_ROOT+'/signin/facebook';
			
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
 
	
 
		<table>
			<tbody align="center">
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
      				<td>
      					<form name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
      						<input type="submit" class="greenButton" value="Login" />
      					</form>
      				</td>
      				<td width="127" height="21">
      					<form action="<c:url value="/signin/facebook" />" method="POST">
		    				<button type="submit" class="greenButton">Login with Facebook</button>
		    				<input type="hidden" name="scope" value="email,publish_stream,offline_access" />		    
						</form>
					</td>
      				<td><input id="signup" type="button" class="greenButton" value="Signup" /></td>
				</tr>
			</tbody>
		</table>
 
	
</body>
</html>