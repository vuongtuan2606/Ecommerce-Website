package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<User> findUserAll();
    List<Role> findRoleAll();
    User save(User user);

    void delete(Integer id) throws UserNotFoundException;

    void encodePassword(User user);

    boolean isEmailUnique(Integer id,String email);

    User findById(Integer id) throws UserNotFoundException;

    void updateUserEnableStatus(Integer id, boolean enabled);



}
