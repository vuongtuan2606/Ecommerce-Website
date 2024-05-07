package com.qtsneaker.ShopFrontEnd.security.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomerOAuth2User implements OAuth2User {

	private String clientName;
	private OAuth2User oauth2User;
	private String fullName;
	public CustomerOAuth2User(String clientName, OAuth2User oauth2User) {
		this.clientName = clientName;
		this.oauth2User = oauth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// Trả về các Attribute của người dùng từ dịch vụ OAuth2
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Trả về danh sách các quyền của người dùng từ dịch vụ OAuth2
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		// Trả về tên của người dùng từ dịch vụ OAuth2
		return oauth2User.getAttribute("name");
	}
	
	public String getEmail() {
		// Trả về địa chỉ email của người dùng từ dịch vụ OAuth2
		return oauth2User.getAttribute("email");
	}

	public String getFullName() {
		// Kiểm tra xem fullName có giá trị không.
		// Nếu có, trả về fullName, nếu không, lấy tên từ thuộc tính của OAuth2 user.
		return fullName != null ? fullName : oauth2User.getAttribute("name");
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getClientName() {
		return clientName;
	}

}
