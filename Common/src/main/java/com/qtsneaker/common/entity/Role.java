package com.qtsneaker.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tbl_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false, unique = true)
    private String name;

    @Column(length = 150, nullable = false)
    private String description;

    public Role(){}

    public  Role(String name){
        this.name = name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
    @Override
    public String toString() {
        return this.name;
    }
    @ManyToMany(
            mappedBy = "roles")
    private Set<User> users = new HashSet<User>();

    public void addUsers(User user) {
        user.getRoles().add(this);
        this.users.add(user);
    }
    public void deleteUsers(User user) {
        user.getRoles().remove(this);
        this.users.remove(user);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
