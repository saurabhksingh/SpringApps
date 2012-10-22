<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var html = '<p><center><b>';
		var blog = $.parseJSON('${blogEntry}');
		//alert(entries);
		html += blog.title + '</center></b><br>';
		html += blog.body;
		html += "<br><br><br>Created By :";
		html += blog.userId;
		html += '</p>';
		$('#blog_posts').append(html);
	});
</script>
<head>
	<title>Home</title>
</head>
<body>
<div align="center">
 <a href="${pageContext.request.contextPath}/userhome">Home</a>
</div>
<div id="blog_posts">
</div>
</body>
</html>