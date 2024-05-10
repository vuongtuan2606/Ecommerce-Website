package com.qtsneaker.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_province ")
public class Province extends IdBasedEntity {

    @Column(name = "name", nullable = false, length = 64)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province() {
    }

    public Province(Integer id) {
        this.id = id;
    }

}
