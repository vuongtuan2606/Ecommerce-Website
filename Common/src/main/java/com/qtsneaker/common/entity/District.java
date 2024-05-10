package com.qtsneaker.common.entity;

import com.qtsneaker.common.entity.IdBasedEntity;
import com.qtsneaker.common.entity.Province;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_district")
public class District extends IdBasedEntity {
    @Column(name = "name", nullable = false, length = 64)
    protected String name;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
