package com.dev.saurabh.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.dev.saurabh.blog.domain.UserAccount;
import com.dev.saurabh.blog.service.UserManagementService;

@RequestMapping("/signup")
@SessionAttributes("user")
@Controller
public class SignUpController {

	private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);
	
	@Resource(name="userMangementService")
	private UserManagementService userManagementService;
	
	@Autowired
    protected AuthenticationManager authenticationManager;
	
	@RequestMapping(method= RequestMethod.GET)
	public String showform(Model model){
        model.addAttribute("user", new UserAccount());
        return "signup";
}
	@RequestMapping(method=RequestMethod.POST)
	public String submitform(@ModelAttribute("user") UserAccount user, BindingResult result, HttpServletRequest request, HttpServletResponse response){
		user.setDeleted(false);
		user.setRole(2);
		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		String password = user.getPassword();
		String hashedPassword = encoder.encode(password);
		user.setPassword(hashedPassword);
		if(userManagementService.create(user))
		{
			//loginUser(user, request, password);
			return "redirect:userhome";
		}
		
		return "signupFail";	
	}
	private void loginUser(UserAccount user, HttpServletRequest request, String password) {
		try {
	        // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
			user.setPassword(password);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
	        token.setDetails(new WebAuthenticationDetails(request));
	        Authentication authentication = this.authenticationManager.authenticate(token);
	        logger.debug("Logging in with [{}]", authentication.getPrincipal());
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	    } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	        logger.error("Failure in autoLogin", e);
	    }
		
	}
}
