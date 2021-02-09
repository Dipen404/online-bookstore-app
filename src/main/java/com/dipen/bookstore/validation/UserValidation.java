package com.dipen.bookstore.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dipen.bookstore.dto.UserDto;
import com.dipen.bookstore.error.UserError;
import com.dipen.bookstore.user.entity.User;
import com.dipen.bookstore.user.repository.UserRepository;
import com.dipen.bookstore.user.service.impl.UserServiceImpl;

@Component
public class UserValidation {

	private UserError userError;
	
	@Autowired
	private UserRepository userRepository;

	public UserError userValidation(UserDto userDto) {
		userError = new UserError();
		userError.setValid(true);

		userError = checkUserError(userDto, userError);

		return userError;
	}

	private UserError checkUserError(UserDto userDto, UserError userError) {
		userError = checkEmail(userDto, userError);
		userError = checkPassword(userDto, userError);
		userError = checkFirstName(userDto, userError);
		userError = checkLastName(userDto, userError);
		userError = checkRepassword(userDto, userError);

		return userError;
	}

	private UserError checkRepassword(UserDto userDto, UserError userError2) {
		if (!(userDto.getPassword()).equals(userDto.getRepassword())) {
			userError.setRepassword("Password does not match");
			userError.setValid(false);
		}

		return userError;

	}

	private UserError checkEmail(UserDto userDto, UserError userError) {
		if (userDto.getEmail() == null || userDto.getEmail().trim().equals("")) {
			userError.setEmail("Email can not be empty");
			userError.setValid(false);
		}
		else if ((checkEmailRegex(userDto.getEmail())) == false) {
			userError.setValid(false);
			userError.setEmail("Invalid email");

		}
		else {
			User user = userRepository.findByEmail(userDto.getEmail());
			if (user != null) {
				userError.setEmail("This username is already registered");
				userError.setValid(false);
			}

		}

		return userError;
	}

	private boolean checkEmailRegex(String email) {
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches() == true) {
			return true;
		} else {
			return false;
		}

	}

	private UserError checkPassword(UserDto userDto, UserError userError) {
		if (userDto.getPassword() == null || userDto.getPassword().trim().equals("")) {
			userError.setPassword("Password can not be empty");
			userError.setValid(false);
		}

		return userError;
	}

	private UserError checkFirstName(UserDto userDto, UserError userError) {
		if (userDto.getFirstName() == null || userDto.getFirstName().trim().equals("")) {
			userError.setFirstName("First name can not be empty");
			userError.setValid(false);
		}

		return userError;
	}

	private UserError checkLastName(UserDto userDto, UserError userError) {
		if (userDto.getLastName() == null || userDto.getLastName().trim().equals("")) {
			userError.setLastName("Last name can not be empty");
			userError.setValid(false);
		}

		return userError;
	}

}
