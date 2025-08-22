package com.thuongmaidientu.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.thuongmaidientu.dto.UserDTO;
import com.thuongmaidientu.service.IKhachHangService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IKhachHangService khachHangService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

	@Override
	public org.springframework.security.core.Authentication authenticate(
			org.springframework.security.core.Authentication authentication) throws AuthenticationException {
		 String username = authentication.getName();
	        String password = authentication.getCredentials().toString();

	        UserDTO dto = khachHangService.login(username);

	        if (dto == null) {
	            throw new BadCredentialsException("Không tìm thấy userName!");
	        }

	        if ("lock".equalsIgnoreCase(dto.getStatusString())) {
	            throw new LockedException("Tài khoản đã bị khóa vui lòng liên hệ để được hỗ trợ!");
	        }

	        if (!passwordEncoder.matches(password, dto.getPassword())) {
	            throw new BadCredentialsException("Mật khẩu không chính xác!");
	        }

	        List<GrantedAuthority> authorities = new ArrayList<>();
	        if ("nhân viên".equals(dto.getRole())) {
	            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	        } else {
	            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	        }

	        return new UsernamePasswordAuthenticationToken(dto, password, authorities);
	}
}
