package com.qtsneaker.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_size")
public class Size extends IdBasedEntity {
    @Column(name = "name", nullable = false, unique = true)
    private Integer name ;

    public Size(){}

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Size(Integer name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Size{" +
                "name=" + name +
                ", id=" + id +
                '}';
    }
}
