<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var items = '<ul>';
		var CONTEXT_ROOT = '<%= request.getContextPath() %>'; 
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
</body>
</html>
