package com.dipen.bookstore.utility;

import org.springframework.stereotype.Component;

import com.dipen.bookstore.dto.UserDto;
import com.dipen.bookstore.user.entity.User;

@Component
public class ConvertUtil {

	public static User convertDtoToUser(UserDto userDto) {

		User user = new User();

		user.setEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setPassword(userDto.getPassword());
		return user;

	}
	
	public static UserDto convertUserToDto(User user) {

		UserDto userDto = new UserDto();

		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		return userDto;

	}

}
