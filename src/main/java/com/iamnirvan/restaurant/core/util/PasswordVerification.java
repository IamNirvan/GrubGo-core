package com.iamnirvan.restaurant.core.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Deprecated
@Component
public class PasswordVerification {
    private final String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

    public boolean verifyPassword(String password) {
        final Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(password).matches();
    }
}
