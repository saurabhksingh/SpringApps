<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<link rel="stylesheet" type="text/css" href="resources/css/demo.css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/script.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var CONTEXT_ROOT = '<%= request.getContextPath() %>'; 
		$("#signup").click(function() {
			document.location.href = CONTEXT_ROOT+'/signup';
			});
		$("#login").click(function() {
			document.location.href = CONTEXT_ROOT+'/login';
			});
	});
</script>
<head>
	<title>Welocome To Free Blogging Space</title>
</head>
<h1>Blog</h1>
<body>
<p>
	A blog (a portmanteau of the term web log)[1] is a discussion or informational site published on the World Wide Web and consisting of discrete entries ("posts") typically displayed in reverse chronological order (the most recent post appears first). Until 2009 blogs were usually the work of a single individual, occasionally of a small group, and often were themed on a single subject. More recently "multi-author blogs" (MABs) have developed, with posts written by large numbers of authors and professionally edited. MABs from newspapers, other media outlets, universities, think tanks, interest groups and similar institutions account for an increasing quantity of blog traffic. The rise of Twitter and other "microblogging" systems helps integrate MABs and single-author blogs into societal newstreams. Blog can also be used as a verb, meaning to maintain or add content to a blog.
The emergence and growth of blogs in the late 1990s coincided with the advent of web publishing tools that facilitated the posting of content by non-technical users. (Previously, a knowledge of such technologies as HTML and FTP had been required to publish content on the Web.)
Although not a requirement, most good quality blogs are interactive, allowing visitors to leave comments and even message each other via GUI widgets on the blogs, and it is this interactivity that distinguishes them from other static websites.[2] In that sense, blogging can be seen as a form of social networking. Indeed, bloggers do not only produce content to post on their blogs, but also build social relations with their readers and other bloggers.[3]
Many blogs provide commentary on a particular subject; others function as more personal online diaries; others function more as online brand advertising of a particular individual or company. A typical blog combines text, images, and links to other blogs, Web pages, and other media related to its topic. The ability of readers to leave comments in an interactive format is an important contribution to the popularity of many blogs. Most blogs are primarily textual, although some focus on art (art blogs), photographs (photoblogs), videos (video blogs or "vlogs"), music (MP3 blogs), and audio (podcasts). Microblogging is another type of blogging, featuring very short posts. In education, blogs can be used as instructional resources. These blogs are referred to as edublogs.
As of 16 February 2011, there were over 156 million public blogs in existence.[4] On October 13, 2012, there were around 77 million Tumblr[5] and 56.6 million WordPress[6] blogs in existence worldwide.
</p>
<div id="buttons" align="center">
<table>
	<tbody>
      	<tr>
      		<td><input id="login" type="button" class="greenButton" value="Login" /><img id="loading" src="resources/images/ajax-loader.gif" alt="working.." /></td>
      		<td><input id="signup" type="button" class="greenButton" value="Signup" /><img id="loading" src="resources/images/ajax-loader.gif" alt="working.." /></td>
		</tr>
     </tbody>
</table>
</div>

</body>
</html>
