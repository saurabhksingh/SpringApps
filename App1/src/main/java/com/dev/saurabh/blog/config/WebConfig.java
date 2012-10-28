package com.dev.saurabh.blog.config;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.dev.saurabh.social.user.UserInterceptor;


@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Inject
	private UsersConnectionRepository usersConnectionRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	public void addInterceptors(InterceptorRegistry registry) {
		logger.info("adding userInterceptor");
		registry.addInterceptor(new UserInterceptor(usersConnectionRepository));
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		logger.info("adding signin-signout to registry");
		//registry.addViewController("/signin");
		registry.addViewController("/signout");
	}

	@Bean
	public ViewResolver viewResolver() {
		logger.info("Inside viewResolver");
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

}
