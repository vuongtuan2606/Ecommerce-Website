package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService  {
    // số phần tử trên 1 trang
    int USERS_PER_PAGE = 5;

    List<User> findUserAll();
    Page <User> listByPage(int pageNum, String sortField, String sortDir,String keyword);
    List<Role> findRoleAll();
    User getByEmail(String email);
    User save(User user);

    User updateAccount(User userInform);

    void delete(Integer id) throws UserNotFoundException;

    void encodePassword(User user);

    boolean isEmailUnique(Integer id,String email);

    User findById(Integer id) throws UserNotFoundException;

    void updateUserEnableStatus(Integer id, boolean enabled);



}
