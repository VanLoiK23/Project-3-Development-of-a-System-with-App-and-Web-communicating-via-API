package com.thuongmaidientu.security;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
	
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String message;

        if (exception instanceof LockedException) {
            message = "Tài khoản đã bị khóa vui lòng liên hệ để được hỗ trợ!";
        } else if (exception instanceof BadCredentialsException) {
            message = exception.getMessage();
        } else {
            message = "Đăng nhập thất bại!";
        }

        // Gán thông báo vào session như flash attribute
        request.getSession().setAttribute("FLASH_MESSAGE", message);

        // Redirect lại trang login (không dùng URL param)
        response.sendRedirect("/Spring-mvc/dang-nhap");
    }
}
