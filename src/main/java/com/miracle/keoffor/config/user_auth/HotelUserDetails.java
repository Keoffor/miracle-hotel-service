package com.miracle.keoffor.config.user_auth;

import com.miracle.keoffor.model.Role;
import com.miracle.keoffor.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private Collection<SimpleGrantedAuthority> authorities;

    public static HotelUserDetails buildUserDetails(User user){
        List<SimpleGrantedAuthority> authorityList = user.getRoles().stream()
                .map(Role::getName).map(SimpleGrantedAuthority::new).toList();
        return new HotelUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(), authorityList);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
