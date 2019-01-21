package com.pl.bas.security;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class SecurityUtilsTest {

    @Test
    public void getCurrentUserLogin() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        assert !currentUserLogin.isPresent();
    }

    @Test
    public void isAuthenticated() {
        boolean authenticated = SecurityUtils.isAuthenticated();
        assert !authenticated;
    }

    @Test
    public void isCurrentUserInRole() {
        boolean isUserInRole = SecurityUtils.isCurrentUserInRole("user");
        assert !isUserInRole;
    }
}
