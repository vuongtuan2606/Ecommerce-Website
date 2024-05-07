package com.qtsneaker.ShopFrontEnd.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomerOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// Lấy tên của client từ thông tin yêu cầu OAuth2
		String clientName = userRequest.getClientRegistration().getClientName();

		// Tải thông tin người dùng từ cơ sở dữ liệu OAuth2
		OAuth2User user = super.loadUser(userRequest);

		// Tạo một đối tượng CustomerOAuth2User mới với thông tin người dùng và tên client
		return new CustomerOAuth2User(clientName,user);
	}


}
