<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>User Signup</title>

<link rel="stylesheet" type="text/css" href="resources/css/demo.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>


</head>

<body>

<div id="div-regForm">

<div class="form-title">Sign Up</div>
<div class="form-sub-title">Join our Blog for free !!</div>

<form:form method="POST" commandName="user" modelAttribute="user" id="signup">

<table>
	<tbody>
      <tr>
       	<td><form:label path="userId">User Name</form:label></td>
        <td> <form:input path="userId" required="true" /></td>
      </tr>
      <tr>
      	<td><form:label path="password">Password</form:label></td>
        <td><form:input type="password" path="password" required="true" /></td>
      </tr>
      <tr>
      	<td><form:label path="firstName">First Name</form:label></td>
      	<td><form:input path="firstName" required="true"/></td>
      </tr>

      <tr>
      	<td><form:label path="lastName">Last Name</form:label></td>
        <td><form:input path="lastName" required="true"/></td>
      </tr>
      <tr>
      <td>&nbsp;</td>
      <td><input type="submit" class="greenButton" value="Sign Up" /></td>
      </tr>
     </tbody>
</table>

</form:form>

<div id="error">
&nbsp;
</div>

</div>

</body>
</html>
