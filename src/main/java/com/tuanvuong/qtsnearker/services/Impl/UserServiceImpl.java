package com.tuanvuong.qtsnearker.services.Impl;

import com.tuanvuong.qtsnearker.dao.RoleRepository;
import com.tuanvuong.qtsnearker.dao.UserRepository;
import com.tuanvuong.qtsnearker.entity.Role;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.UserService;
import com.tuanvuong.qtsnearker.services.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> findUserAll() {
        return (List<User>) userRepo.findAll();
    }


    @Override
    public Page<User> listByPage(int pageNum, String sortField, String sortDir , String keyword) {

        Sort sort = Sort.by(sortField);

        // Nếu sortDir là "asc"  sắp xếp tăng dần ngược lại
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

        if(keyword != null){
            return userRepo.findAll(keyword, pageable);
        }

        return  userRepo.findAll(pageable);
    }

    @Override
    public List<Role> findRoleAll() {
        return (List<Role>) roleRepo.findAll();
    }

    @Override
    public User getByEmail(String email) {
        return userRepo.getUserByEmail(email);
    }


    @Override
    public void encodePassword(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
    }

    @Override
    public boolean isEmailUnique(Integer id, String email) {

        User userByEmail = userRepo.getUserByEmail(email);

        // - if userByEmail null return true
        // - không tìm thấy email đã nhập cho trong db

        // - if userByEmail not null return false
        // - đã tồn tại email đã nhập trong db
        if(userByEmail == null) return true;

        // nếu id == null là đang ở thêm mới
        boolean isCreatingNew = (id == null);

        if(isCreatingNew){
            // Nếu userByEmail không rỗng -> return false
            // Vì khi thêm mới đã có người dùng có email này (tính duy nhất)
            if(userByEmail != null) return false;
        }
        else {
            // so sánh id của người hiện tại với id của userByEmail
            // nếu giống nhau thì người này đang cập nhật tt của mình
            // nếu khác nhau thì đã có người dùng khác sử dụng email này
            if (userByEmail.getId() != id){
                return  false;
            }
        }
        return true;
    }

    @Override
    public User findById(Integer id) throws UserNotFoundException {

        try{
            return userRepo.findById(id).get();
        }
        catch (NoSuchElementException ex){
            throw  new UserNotFoundException("Không tìm thấy người dùng có id"+id);
        }

    }

    @Override
    public User save(User user) {
        // Kiểm tra xem đây có phải là việc cập nhật người dùng không
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
           User existingUser = userRepo.findById(user.getId()).get();

           if(user.getPassword().isEmpty()){
               // Nếu mật khẩu không được cung cấp, giữ nguyên mật khẩu cũ
               user.setPassword(existingUser.getPassword());
           }
           else {
               // Nếu mật khẩu được cung cấp, mã hóa mật khẩu mới
               encodePassword(user);
           }
        }
        else {
            encodePassword(user);
        }
        // Lưu hoặc cập nhật đối tượng người dùng vào cơ sở dữ liệu
        return userRepo.save(user);
    }

    @Override
    public User updateAccount(User userInform) {
        User userInDB = userRepo.findById(userInform.getId()).get();

        if(!userInform.getPassword().isEmpty()){
            userInDB.setPassword(userInform.getPassword());
            encodePassword(userInDB);
        }

        if(userInform.getPhotos() != null){
            userInDB.setPhotos(userInform.getPhotos());
        }

        userInDB.setFirstName(userInform.getFirstName());
        userInDB.setLastName(userInform.getLastName());

        return userRepo.save(userInDB);

    }


    @Override
    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);

        if(countById == null || countById == 0){
            throw  new UserNotFoundException("Không tìm thấy người dùng có id: "+id);
        }
        // Xóa người dùng có id đã cho khỏi db
        userRepo.deleteById(id);
    }

    @Override
    public void updateUserEnableStatus(Integer id, boolean enabled) {

        userRepo.updateEnabledStatus(id, enabled);
    }



}
