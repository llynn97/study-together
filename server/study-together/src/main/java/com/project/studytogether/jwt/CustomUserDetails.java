package com.project.studytogether.jwt;

import com.project.studytogether.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
    private final User user;
    private Map<String, Object> attributes;

    public CustomUserDetails(User user) {
        this.user = user;

    }

    public static CustomUserDetails create(User user) {

        return new CustomUserDetails(
                user
        );
    }

    public static CustomUserDetails create(User user, Map<String, Object> attributes) {
        CustomUserDetails userDetails = CustomUserDetails.create(user);
        userDetails.setAttributes(attributes);
        return userDetails;
    }

    @Override
    public String getName() {
        return user.getId();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

}
