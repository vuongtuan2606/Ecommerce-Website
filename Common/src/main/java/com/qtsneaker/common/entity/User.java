package com.qtsneaker.common.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name="tbl_users")
public class User extends IdBasedEntity {

    @Column(length = 128, nullable = false, unique = true)
    private  String email;
    @Column(length = 64, nullable = false)
    private  String password;
    @Column(name="first_name", length = 45, nullable = false)
    private  String firstName;
    @Column(name="last_name", length = 45, nullable = false)
    private  String lastName;
    @Column(length = 64)
    private  String photos;
    private boolean enabled ;

    public User(){}

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<Role> roles = new HashSet<Role>();

    public void addRoles(Role role ) {
        role.getUsers().add(this);
        roles.add(role);
    }
    public void deleteRole(Role role) {
        role.getUsers().remove(this);
        roles.remove(role);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }



    /* @Transient
    * chỉ định rằng trường hoặc phương thức
    * không nên được ánh xạ vào cơ sở dữ liệu thông qua JPA
     * */
    @Transient
    public String getPhotosImagePath(){
        // sủ dụng ảnh mặc định/
        if(id == null || photos == null) return "/images/avatar-default.jpg";

        return "/user-photos/" + this.id + "/" +this.photos;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photos='" + photos + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }

    /*Iterator

     Sử dụng để lặp qua các phần tử của một tập hợp mà không cần biết về cấu trúc nội bộ của tập hợp đó.
     Giao diện Iterator cho phép truy cập tuần tự đến các phần tử trong một tập hợp
     và thực hiện các thao tác như lấy phần tử tiếp theo hoặc kiểm tra xem còn phần tử nào nữa không.

     Iterator cung cấp các phương thức sau:

       - boolean hasNext(): Kiểm tra xem tập hợp có phần tử tiếp theo hay không.
       - E next(): Trả về phần tử tiếp theo trong tập hợp.
       - (Tùy chọn) void remove(): Xóa phần tử cuối cùng được trả về bởi phương thức next() từ tập hợp.

    Thường sử dụng lặp qua các phần tử của các cấu trúc dữ liệu như ArrayList, LinkedList, HashSet, và TreeMap
    * */
    public  boolean hasRole(String roleName){
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()){
            Role role = iterator.next();
            if(role.getName().equals(roleName)){
                return true;
            }
        }
        return false;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
