<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<link rel="stylesheet" type="text/css" href="../../resources/css/demo.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="../../resources/js/script.js"></script>
<script type="text/javascript">
	//var jq = jQuery.noConflict();
	$(document).ready(function() {
		var html = '<p><center><b>';
		var blog = $.parseJSON('${blogEntry}');
		//alert(entries);
		html += blog.title + '</center></b><br>';
		html += blog.body;
		html += '</p>';
		//alert(blog.title);
		$('#blog_posts').append(html);
		$('input[name="title"]').val(blog.title);
		//alert($('input[name="title"]').val());
		$('input[name="link"]').val(location.href);
		$('#shareFB').click(function() {
			var title = $('input[name="title"]').val();
			var link = $('input[name="link"]').val() ;
			var CONTEXT_ROOT = '<%= request.getContextPath() %>'; 
			var postUrl = CONTEXT_ROOT+"/shareOnFB";//"/App1/shareOnFB"
			//alert(postUrl);
			$.ajax({
		        type: "POST",
		        url: postUrl,
		        data: "title=" + title + "&link=" + link,
		        success: function(response){
		       alert(response)
		        },
		        error: function(e){
		        alert('Error: ' + e);
		        }
		        });
		});
		
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
<form name="blogPostData">
		 <input id="shareFB" type="button" class="blueButton" value="Share on Facebook"/>
		 <input name="title" type="hidden"/>
		 <input name="link" type="hidden"/>
	</form>
</body>
</html>