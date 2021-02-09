package com.dipen.bookstore.user.service.impl;

import com.dipen.bookstore.user.entity.PasswordResetToken;
import com.dipen.bookstore.user.entity.Role;
import com.dipen.bookstore.user.entity.User;
import com.dipen.bookstore.user.entity.UserRole;
import com.dipen.bookstore.user.repository.PasswordResetTokenRepo;
import com.dipen.bookstore.user.repository.RoleRepository;
import com.dipen.bookstore.user.repository.UserRepository;
import com.dipen.bookstore.user.repository.UserRoleRepository;
import com.dipen.bookstore.user.service.UserService;
import com.dipen.bookstore.utility.JavaMail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepo.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepo.save(myToken);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws AddressException, MessagingException, IOException {
        log.debug("Creating new user {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()) != null) {
            log.error("Duplicate Email: {}", user.getEmail());
        }else {
            user =  userRepository.save(user);
            for (UserRole userRole : userRoles)
                userRole.setUser(user);
            userRoleRepository.saveAll(userRoles);
        }
        
        JavaMail.sendmail(user.getEmail());

       return user;
    }

	@Override
	public void resetPassword(String email, String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = userRepository.findByEmail(email);
		
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}
}
