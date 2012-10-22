package com.dev.saurabh.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dev.saurabh.blog.domain.BlogEntry;
import com.dev.saurabh.blog.service.BlogService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BlogApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(BlogApplicationController.class);
	
	@Resource(name="blogService")
	private BlogService appService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		logger.info("date is "+ formattedDate);
		model.addAttribute("userId", "Guest" );
		
		return "index";
	}
	
	@RequestMapping(value="/userhome", method = RequestMethod.GET)
	public String printWelcome(Model model, Principal principal ) {
		if(principal != null)
		{
			List <BlogEntry> entries = appService.getMyBlogEntries(principal.getName());
			ObjectMapper mapper = new ObjectMapper();
			try
			{
				String json = mapper.writeValueAsString(entries);
				model.addAttribute("blogEntries", json);
			}
			catch(Exception exc)
			{
				logger.error("Error occurred while creating json form of blog entries", exc);
			}
			model.addAttribute("userId", principal.getName());
				
		}
		else
		{
			model.addAttribute("userId", "Guest");
		}
		return "userhome";
 
	}
	
	@RequestMapping(value="/addBlogPost", method = RequestMethod.GET)
	public String showBlogPostAddPage(Model model, Principal principal ) {
		if(principal == null || principal.getName() == null)
		{
			return "login";
		}
		model.addAttribute("blogEntry", new BlogEntry());
		
		return "addBlog";
 
	}
	
	@RequestMapping(value="/addBlogPost", method=RequestMethod.POST)
	public String addBlogPost(@ModelAttribute("blogEntry") BlogEntry blogEntry, BindingResult result, Principal principal){
		long createTime = new Date().getTime();
		blogEntry.setCreationTime(createTime);
		blogEntry.setUpdateTime(createTime);
		blogEntry.setUserId(principal.getName());
		
		if(appService.addBlogEntry(blogEntry))
		{
			//loginUser(user, request, password);
			return "redirect:addBlogPost";
		}
		
		return "signupFail";	
	}
	
	@RequestMapping(value="/view/blog/{blogId}", method=RequestMethod.GET)
	public String displayBlogPost(@PathVariable String blogId, Model model) {
	  logger.info("fetch the blog title and body"); 
	  BlogEntry blogEntry = appService.getBlogEntryById(blogId);
	  ObjectMapper mapper = new ObjectMapper();
		try
		{
			String json = mapper.writeValueAsString(blogEntry);
			model.addAttribute("blogEntry", json);
		}
		catch(Exception exc)
		{
			logger.error("Error occurred while creating json form of blog entries", exc);
		} 
	  return "blogPost"; 
	}
}
