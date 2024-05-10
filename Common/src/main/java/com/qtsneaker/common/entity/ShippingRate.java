package com.qtsneaker.common.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "tbl_shipping_rates")
public class ShippingRate extends IdBasedEntity {

	private float rate;
	private int days;
	
	@Column(name = "cod_supported")
	private boolean codSupported;

	@ManyToOne
	@JoinColumn(name = "province_id")
	private Province province;
	
	@Column(nullable = false, length = 45)
	private String district;

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isCodSupported() {
		return codSupported;
	}

	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}



	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return "Giá vận chuyển [id=" + id + ", giá=" + rate + ", ngày=" + days + ", codSupported=" + codSupported
				+  ", huyện=" + district + " , tỉnh/ thành phố=" + province + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShippingRate other = (ShippingRate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	

	
}
