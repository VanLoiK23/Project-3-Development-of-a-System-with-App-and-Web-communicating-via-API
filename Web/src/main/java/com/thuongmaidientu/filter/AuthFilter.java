package com.thuongmaidientu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/quan-tri/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Object user = (session != null) ? session.getAttribute("user") : null;

        if (user == null) {
            // Chưa đăng nhập → chuyển hướng về trang chủ
            res.sendRedirect(req.getContextPath() + "/trang-chu");
            return;
        }

        // Cho phép tiếp tục xử lý nếu đã đăng nhập
        chain.doFilter(request, response);
    }
}
