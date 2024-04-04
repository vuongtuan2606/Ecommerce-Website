package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserService {
    List<User> findUserAll();
    List<Role> findRoleAll();
    void save(User user);


    void encodePassword(User user);

    boolean isEmailUnique(Integer id,String email);

    User findById(Integer id) throws UserNotFoundException;

}
