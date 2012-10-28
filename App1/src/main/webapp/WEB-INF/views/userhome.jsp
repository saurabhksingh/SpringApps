<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="resources/css/demo.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var CONTEXT_ROOT = '<%= request.getContextPath() %>'; 
		$("#addPost").click(function() {
			
			document.location.href = CONTEXT_ROOT+'/addBlogPost';
		});
		$("#logout").click(function() {
			
			document.location.href = '<c:url value="j_spring_security_logout" />';
		});
		var items = '<ul>';
		var entries = $.parseJSON('${blogEntries}');
		//alert(entries);
		$.each(entries, function(key,val){
			  //alert(val.title);
		      items += ('<li><a href="'+CONTEXT_ROOT+'/view/blog/'+val.blogId +'">'+val.title+'</a></li>');
		      
		    });
		items += '</ul>';
		$('#blog_posts').append(items);
	});
</script>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Welcome ${userId}  
</h1>
<div id="blog_posts">
</div>
<div align="center">
<table>
	<tbody align="center">
      	<tr>
      		<td><input id="addPost" type="button" class="blueButton" value="Add Post" /></td>
      		<td><input id="logout" type="button" class="blueButton" value="Logout" /></td>
		</tr>
     </tbody>
</table>
</div>
</body>
</html>
