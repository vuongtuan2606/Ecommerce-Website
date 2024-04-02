package com.tuanvuong.qtsnearker.services;

import com.tuanvuong.qtsnearker.dao.UserRepository;
import com.tuanvuong.qtsnearker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;

    @Override
    public List<User> findAll() {
        return repo.findAll();
    }
}
