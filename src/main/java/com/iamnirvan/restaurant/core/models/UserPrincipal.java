package com.iamnirvan.restaurant.core.models;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * This represents the currently logged user...
 * */
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final Principal principal;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(principal.getAccount().getRoles().getName()));
    }

    @Override
    public String getPassword() {
        return principal.getAccount().getPassword();
    }

    @Override
    public String getUsername() {
        return principal.getAccount().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//        return principal.getAccount().getActive();
        return true;
    }
}
