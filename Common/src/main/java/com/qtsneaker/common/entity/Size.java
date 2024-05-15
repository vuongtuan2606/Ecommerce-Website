package com.qtsneaker.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_size")
public class Size extends IdBasedEntity {
    @Column(nullable = false, length = 45, unique = true)
    private String name;

    public Size(){}
    public Size(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Size{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
