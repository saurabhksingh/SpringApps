<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html>
<head>
<title>Add Blog Post</title>
<link rel="stylesheet" type="text/css" href="resources/css/skins/markitup/style.css" />
<link rel="stylesheet" type="text/css" href="resources/css/reveal.css" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.9.0/themes/base/jquery-ui.css" />
<link type='text/css' href='css/popup/demo.css' rel='stylesheet' media='screen' />
<link type='text/css' href='css/popup/basic.css' rel='stylesheet' media='screen' />
<script type="text/javascript" src="resources/js/script.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/js/sets/default/style.css" />
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.markitup.js"></script>
<script type="text/javascript" src="resources/js/sets/default/set.js"></script>
<script type='text/javascript' src='resources/js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='resources/js/basic.js'></script>
<script src="http://code.jquery.com/ui/1.9.0/jquery-ui.js"></script>
<script type="text/javascript" src="resources/js/jquery.bigframe.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#body').markItUp(mySettings);
		var dialog = $('#dialog-modal');
		dialog.dialog({              // <-- create the dialog
			 height: 500,
	         width:700,
	         modal: true,
	        autoOpen: false
	    });
		
		$("#previewBlogPost").click(function() {
				$('#dialog-modal').html(function() { 
						var content = '<p><center><b>' + $('#title').val() + '</b></center><br>' + $('#body').val() + '</p>';
						//alert(content);
					  	return content;
					});
				dialog.dialog('open');
			});
	});
</script>

</head>
<body>

	<div id="div-regForm">

		<div class="form-sub-title">Write you blog post here</div>
		<br>
		<br>
		<form:form method="POST" commandName="blogEntry"
			modelAttribute="blogEntry" id="addBlogPost">

			<table>
				<tbody>
					<tr>
						<form:textarea id="title" path="title" cols="80" rows="2" required="true"/>
					</tr>
					<tr>
						<form:textarea id="body" path="body" cols="80" rows="20"/>
					</tr>
					<tr align="center">
						<td><input id="postBlogEntry" type="submit" class="blueButton" value="Post" /></td>
						<td><input id="previewBlogPost" type="button" class="blueButton" value="Preview" /></td>
					</tr>
				</tbody>
			</table>

		</form:form>

		<div id="error">&nbsp;</div>
	</div>
	<div id="dialog-modal" class="messi-modal" title="This is how it will look like !!">
   	</div>
</body>
</body>
</html>