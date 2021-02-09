package com.dipen.bookstore.user.controller;

import com.dipen.bookstore.security.UserAuthenticationService;
import com.dipen.bookstore.user.entity.PasswordResetToken;
import com.dipen.bookstore.user.entity.User;
import com.dipen.bookstore.user.repository.UserRepository;
import com.dipen.bookstore.user.service.UserService;
import com.dipen.bookstore.utility.AuthenticationUtil;
import com.dipen.bookstore.utility.ConvertUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/new")
    public String newUser(@RequestParam("token") final String token, Model model ) {
        PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

        if(passwordResetToken == null) {
            model.addAttribute("message", "Invalid token!");
            return "redirect:/badRequest";
        }

        User user = passwordResetToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userAuthenticationService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("classActiveEdit", true);

        return "user/userProfile";


    }

    @GetMapping("/profile")
    public String profile(ModelMap modelMap) {
    	String username =AuthenticationUtil.getLoggedInUser();
    	modelMap.addAttribute("useDto", ConvertUtil.convertUserToDto(userRepository.findByEmail(username)));
        return "user/userProfile";
    }
}
