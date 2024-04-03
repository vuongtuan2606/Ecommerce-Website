package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.dao.RoleRepository;
import com.tuanvuong.qtsnearker.dao.UserRepository;
import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> findUserAll() {
        return userRepo.findAll();
    }

    @Override
    public List<Role> findRoleAll() {
        return roleRepo.findAll();
    }

    @Override
    public void save(User user) {
        encodePassword(user);
        userRepo.save(user);
    }

    @Override
    public void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }


}
