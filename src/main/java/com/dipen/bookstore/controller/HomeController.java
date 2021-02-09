package com.dipen.bookstore.controller;

import com.dipen.bookstore.books.entity.Book;
import com.dipen.bookstore.books.service.impl.BookServiceImpl;
import com.dipen.bookstore.books.service.impl.CategoryServiceImpl;
import com.dipen.bookstore.dto.UserDto;
import com.dipen.bookstore.error.UserError;
import com.dipen.bookstore.user.entity.Role;
import com.dipen.bookstore.user.entity.User;
import com.dipen.bookstore.user.entity.UserRole;
import com.dipen.bookstore.user.repository.UserRepository;
import com.dipen.bookstore.user.service.RoleService;
import com.dipen.bookstore.user.service.impl.UserServiceImpl;
import com.dipen.bookstore.validation.UserValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

// https://programmer.ink/think/spring-boot-spring-security-thymeleaf-simple-tutorial.html
@Controller
public class HomeController {

	@Autowired
	private CategoryServiceImpl categoryService;
	@Autowired
	private BookServiceImpl bookService;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private UserValidation userValidation;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRepository userRepository;

	/*
	 * @GetMapping("/") public String goHome(Model model, Authentication
	 * authentication) { model.addAttribute("categoryList",
	 * categoryService.getAllCategory()); model.addAttribute("bookList",
	 * bookService.getAllBooks()); model.addAttribute("selectedCategory", "0");
	 * return "index"; }
	 */

	@GetMapping("/my-account")
	public String myAccount() {
		return "myAccount";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("error", new UserError());
		model.addAttribute("user", new UserDto());
		return "register";
	}

	@GetMapping("/forget-password")
	public String forgetPassword(Model model) {
		model.addAttribute("error", "");
		return "forgetPassword";
	}

	@GetMapping("/create-account")
	public String createAccount(Model model, Principal principal) {
		return "myAccount";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute UserDto userDto, ModelMap modelMap)
			throws AddressException, MessagingException, IOException {
		UserError userError = new UserError();
		userError = userValidation.userValidation(userDto);

		if (userError.isValid()) {
			Role role = roleService.findOrCreateRole("ROLE_USER");
			Set<UserRole> userRoles = new HashSet<UserRole>();
			// User user = ConvertUtil.convertDtoToUser(userDto);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
					userDto.getFirstName(), userDto.getLastName());
			// user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRoles.add(new UserRole(user, role));
			userService.createUser(user, userRoles);
			// userService.createUser(userDto);
			return "login";

		} else {

			modelMap.addAttribute("error", userError);
			modelMap.addAttribute("user", userDto);
			return "register";
		}
	}

	@GetMapping("/")
	public String goHome(Model model, Authentication authentication,
			@RequestParam(name = "pageno", defaultValue = "0") int pageNo) {
		Page<Book> pagedResult = bookService.getPaginatedBook(pageNo, 6);
		model.addAttribute("categoryList", categoryService.getAllCategory());
		model.addAttribute("bookList", pagedResult.toList());
		model.addAttribute("totalPages", pagedResult.getTotalPages());
		model.addAttribute("selectedCategory", "0");
		return "index";
	}

	@PostMapping("/forget-password")
	public String forgetpass(@RequestParam(value = "email", required = true) String email, ModelMap modelMap) {
		if(email == null || email.trim().equals("")) {
			modelMap.addAttribute("error", "Email address is required");
			return "forgetPassword";
		}
		User user = userRepository.findByEmail(email);
		if(user==null) {
			modelMap.addAttribute("error", "Email address is not registered");
			return "forgetPassword";
		}
		modelMap.addAttribute("email", email);
		return "resetPassword";
		
	}

	@PostMapping("/reset-password")
	public String resetPass(@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "repassword", required = true) String repassword, ModelMap modelMap) {
		if(password == null || password.trim().equals("")) {
			modelMap.addAttribute("passerror", "New password is required");
			return "resetPassword";
		}
		if(!(password.equals(repassword))) {
			modelMap.addAttribute("password", password);
			modelMap.addAttribute("repassword", repassword);
			modelMap.addAttribute("repasserror", "Password does not match");
			return "resetPassword";
		}
		userService.resetPassword(email, password);
		return "login";
		
	}

}
