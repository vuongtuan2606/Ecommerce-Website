package com.qtsneaker.ShopBackEnd.services.User;

import com.qtsneaker.ShopBackEnd.exception.UserNotFoundException;
import com.qtsneaker.common.entity.Role;
import com.qtsneaker.common.entity.User;

import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminUserService {
    // số phần tử trên 1 trang
    public static final int USERS_PER_PAGE = 5;

    List<User> findUserAll();
    Page <User> listByPage(int pageNum, String sortField, String sortDir, String keyword);
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
