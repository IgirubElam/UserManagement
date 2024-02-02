package com.example.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.UserDtls;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		
		if(p!=null)
		{
			String email = p.getName();
			UserDtls user = userRepository.findByEmail(email);
			m.addAttribute("user", user);
		}
		
		
	}
	
	@GetMapping("/")
	public String index()
	{
		return "index";
	}
	
	@GetMapping("/signin")
	public String login()
	{
		return "login";
	}
	
	@GetMapping("/register")
	public String register()
	{
		return "register";
	}
	
	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user, RedirectAttributes redirectAttributes) {
		
		//System.out.println(user);
		
		boolean emailExists = userService.checkEmail(user.getEmail());
		
		if(emailExists) {
			redirectAttributes.addFlashAttribute("error", "Email is already exists");
		}else {
			UserDtls userDetails = userService.createUser(user);
			if(userDetails != null) {
				redirectAttributes.addFlashAttribute("success", "Register successfully");
			} else {
				redirectAttributes.addFlashAttribute("error", "Something wrong on server");
			}
		}
		
		
		
		return "redirect:/register";
	}
}
