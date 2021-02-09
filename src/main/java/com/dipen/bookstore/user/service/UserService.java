package com.dipen.bookstore.user.service;

import org.springframework.stereotype.Service;

import com.dipen.bookstore.user.entity.PasswordResetToken;
import com.dipen.bookstore.user.entity.Role;
import com.dipen.bookstore.user.entity.User;
import com.dipen.bookstore.user.entity.UserRole;

import java.io.IOException;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

@Service
public interface UserService {
    PasswordResetToken getPasswordResetToken(final String token);
    void createPasswordResetTokenForUser(final User user, final String token);
    User createUser(User user, Set<UserRole> userRoles) throws AddressException, MessagingException, IOException;
    void resetPassword(String email, String password);
}
