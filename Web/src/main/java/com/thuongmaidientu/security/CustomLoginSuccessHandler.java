package com.thuongmaidientu.security;

import java.io.IOException;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.thuongmaidientu.dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

   
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
		 UserDTO dto = (UserDTO) authentication.getPrincipal();
	        request.getSession().setAttribute("user", dto);
	        request.getSession().setAttribute("name", dto.getUsername());
	        
	        request.getSession().setAttribute("FLASH_MESSAGE", "Đăng nhập thành công!");

	        
	        if ("nhân viên".equals(dto.getRole())) {
	            response.sendRedirect("/Spring-mvc/quan-tri/Homepage_admin");
	        } else {
	            response.sendRedirect("/Spring-mvc/trang-chu");
	        }		
	}
}
