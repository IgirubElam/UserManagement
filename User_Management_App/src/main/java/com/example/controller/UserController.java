package com.example.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.UserDtls;
import com.example.repository.UserRepository;

@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		
		m.addAttribute("user", user);
	}
	
	
	@GetMapping("/")
	public String home() {
		return "user/home";
	}
	
	@GetMapping("/changePass")
	public String loadChangePassword() {
		return "user/change_password";
	}
	
	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass, 
			@RequestParam("newPass") String newPass, RedirectAttributes redirectAttributes) {
		
		String email = p.getName();
		
		UserDtls loginUser = userRepository.findByEmail(email);
		
		boolean changingPassword = passwordEncoder.matches(oldPass, loginUser.getPassword());
		
		if(changingPassword) {
			
			loginUser.setPassword(passwordEncoder.encode(newPass));
			UserDtls updatePasswordUser = userRepository.save(loginUser);
			
			if(updatePasswordUser!=null) {
				
				redirectAttributes.addFlashAttribute("success", "Password change success");
				
			}else {
				
				redirectAttributes.addFlashAttribute("error", "Something wrong on server");
				
			}
			
		}else {
			redirectAttributes.addFlashAttribute("error", "Old Password incorrect");
		}
		
		return "redirect:/user/changePass";
	}
}
