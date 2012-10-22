package com.dev.saurabh.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.dev.saurabh.blog.domain.BlogEntry;


@Transactional
public class AppBootstrapService {

	private static final Logger logger = LoggerFactory.getLogger(AppBootstrapService.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@SuppressWarnings("unused")
	private void init()
	{
		logger.info("Bootstrap the blog database");
		if (!mongoTemplate.collectionExists(BlogEntry.class)) {
            mongoTemplate.createCollection(BlogEntry.class);
        }
	}
}
