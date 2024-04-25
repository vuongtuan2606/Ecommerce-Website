package com.tuanvuong.qtsnearker.security;

import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AdminUserDetails implements UserDetails {


    private User user;

    public AdminUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set< Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
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
        return user.isEnabled();
    }

    public void setFirstName(String firstName){
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName){
        this.user.setLastName(lastName);
    }

    public void  setPhoto(String photo){
        this.user.setPhotos(photo);
    }

    public String getFullName(){
        return this.user.getFirstName() + " "+this.user.getLastName();
    }

    public  String getPhoto(){
        return this.user.getPhotosImagePath();
    }

    public boolean hasRole(String roloName){
        return user.hasRole(roloName);
    }

}
