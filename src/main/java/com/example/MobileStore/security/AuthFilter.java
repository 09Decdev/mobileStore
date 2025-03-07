package com.example.MobileStore.security;

import com.example.MobileStore.entity.User;
import com.example.MobileStore.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

    @Component // Đánh dấu class này là một Spring Bean
    public class AuthFilter extends OncePerRequestFilter {
        @Autowired
        private JwtTokenProvider tokenProvider; // Bean dùng để xử lý JWT

        @Autowired
        @Lazy
        private UserService userService; // Bean để lấy thông tin người dùng từ database

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull FilterChain filterChain) throws ServletException, IOException {
            // Lấy header Authorization từ request
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Kiểm tra nếu header Authorization có chứa token dạng "Bearer <token>"
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                token = authHeader.substring(7); // Cắt bỏ phần "Bearer " để lấy token thực sự
                username = tokenProvider.extractUsername(token); // Giải mã lấy username từ token
            }

            // Nếu lấy được username và chưa có người dùng nào đăng nhập trong SecurityContextHolder
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Lấy thông tin người dùng từ database
                User principal = userService.getUserByUsername(username)
                        .orElseThrow(() -> new BadCredentialsException("Username not found"));

                // Kiểm tra tính hợp lệ của token
                if (tokenProvider.validateToken(token, principal)) {
                    // Tạo đối tượng xác thực cho Spring Security
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    // Đặt thông tin xác thực vào SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Tiếp tục cho request đi qua các bộ lọc khác
            filterChain.doFilter(request, response);
        }
    }
