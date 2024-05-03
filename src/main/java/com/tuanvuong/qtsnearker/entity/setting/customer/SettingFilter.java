package com.tuanvuong.qtsnearker.entity.setting.customer;


import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.services.customer.SettingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class SettingFilter implements Filter {

	@Autowired
	private SettingService service;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest servletRequest = (HttpServletRequest) request;

		String url = servletRequest.getRequestURL().toString();

		if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith("png")){
			chain.doFilter(request,response);
			return;
		}

		List<Setting> generalSetting = service.getGeneralSettings();

		generalSetting.forEach(setting -> {
			System.out.println(setting);
			request.setAttribute(setting.getKey(), setting.getValue());
		});

		chain.doFilter(request,response);
	}
}
