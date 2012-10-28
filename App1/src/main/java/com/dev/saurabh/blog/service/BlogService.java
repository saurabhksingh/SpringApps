package com.dev.saurabh.blog.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.saurabh.blog.domain.BlogEntry;


@Service("blogService")
@Transactional
public class BlogService {
	
	private static final Logger logger = LoggerFactory.getLogger(BlogService.class);
	
	private static final String BLOG_ID = "blogId";
	private static final String USER_ID = "userId";
	private static final String TITLE = "title";
	private static final String BODY = "body";
	private static final String UPDATED_AT = "updated_at";
	
	@Resource(name="mongoTemplate")
	private MongoTemplate mongoTemplate;
	
	public List<BlogEntry> getAllBlogEntries()
	{
		logger.info("retrieve all the blog entries");
		
		Query query = new Query(Criteria.where(BLOG_ID).exists(true));
		
		return mongoTemplate.find(query, BlogEntry.class);
	}
	
	
	public List<BlogEntry> getMyBlogEntries(String userId)
	{
		logger.info("retrieve the blog entries of : "+userId);
		
		Query query = new Query(Criteria.where(BLOG_ID).exists(true).and(USER_ID).is(userId));
		query.fields().include(BLOG_ID).include(TITLE).include(UPDATED_AT);
		return mongoTemplate.find(query, BlogEntry.class);
	}
	
	public List<BlogEntry> getBlogEntriesContainingInTitle(String text)
	{
		logger.info("retrieve the blog entries having \""+text + "\" in title");
		
		Query query = new Query(Criteria.where(TITLE).regex("*"+text+"*"));
		
		return mongoTemplate.find(query, BlogEntry.class);
	}
	
	public BlogEntry getBlogEntryById(String id)
	{
		logger.info("retrieve the blog entry with id : "+id);
		Query query = new Query(Criteria.where(BLOG_ID).is(id));
		return mongoTemplate.findOne(query, BlogEntry.class);
		
	}
	
	public boolean addBlogEntry(BlogEntry content)
	{
		logger.info("add the blog entry");
		content.setBlogId(UUID.randomUUID().toString());
		try
		{
			mongoTemplate.insert(content, mongoTemplate.getCollectionName(BlogEntry.class));
			
			return true;
		}
		catch(Exception exc)
		{
			logger.error("An error has occurred while trying to add new user", exc);
		}
		
		return false;
	}
	
	public boolean deleteBlogEntry(String id)
	{
		logger.info("Deletying the entry with id : "+id);
		
		try
		{
			Query query = new Query(Criteria.where(BLOG_ID).is(id));
			mongoTemplate.remove(query, mongoTemplate.getCollectionName(BlogEntry.class));
			
			return true;
		}
		catch(Exception exc)
		{
			logger.error("Error occurred while deleting the record : ",exc);
		}
		
		return false;
	}
	
	public boolean editBlogEntry(BlogEntry newContent)
	{
		logger.info("Editing the exisiting blog entry : "+newContent.getBlogId());
		
		try
		{
			Query query = new Query(Criteria.where(BLOG_ID).is(newContent.getBlogId()));
			Update update = new Update();
			
			update.set(TITLE, newContent.getTitle());
			mongoTemplate.updateMulti(query, update, BlogEntry.class);
			
			update.set(BODY, newContent.getBody());
			mongoTemplate.updateMulti(query, update, BlogEntry.class);
			
			update.set(UPDATED_AT, (new Date()).getTime());
			mongoTemplate.updateMulti(query, update, BlogEntry.class);
			
			return true;
		}
		catch(Exception exc)
		{
			logger.error("Exception occurred while updating the entry", exc);
		}
		
		return false;
	}

}
