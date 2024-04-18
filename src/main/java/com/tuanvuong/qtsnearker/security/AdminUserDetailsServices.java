package com.tuanvuong.qtsnearker.security;

import com.tuanvuong.qtsnearker.dao.UserRepository;
import com.tuanvuong.qtsnearker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AdminUserDetailsServices implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user != null){
            return new AdminUserDetails(user);
        }
        throw  new UsernameNotFoundException("không tìm thấy người dùng có email:" +email);
    }
}
