package com.example.MobileStore.config;

import com.example.MobileStore.repository.UserRepository;
import com.example.MobileStore.security.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration // Đánh dấu lớp này là một lớp cấu hình Spring
@EnableWebSecurity // Kích hoạt tính năng bảo mật Web Security của Spring Security
public class SecurityConfig {

    @Autowired
    private AuthFilter authFilter; // Bộ lọc xác thực tùy chỉnh

    @Autowired
    private UserRepository userRepository; // Repository để lấy thông tin người dùng từ DB

    /**
     * Cấu hình SecurityFilterChain để thiết lập các quy tắc bảo mật.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Tắt CSRF để tránh lỗi khi làm việc với API REST
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Thêm AuthFilter trước bộ lọc UsernamePasswordAuthenticationFilter để xử lý xác thực
        httpSecurity.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        // Định nghĩa các quyền truy cập URL cụ thể
        httpSecurity.authorizeHttpRequests(auths -> auths
                .requestMatchers("api/users/**").permitAll() // Cho phép tất cả truy cập vào user API
                .requestMatchers("api/orders/**").hasRole("MEMBER") // Chỉ người dùng có vai trò MEMBER mới truy cập được
                .requestMatchers("api/employee/**").hasRole("ADMIN") // Chỉ người dùng có vai trò ADMIN mới truy cập được
                .anyRequest().authenticated()); // Các yêu cầu khác phải được xác thực

        return httpSecurity.build();
    }

    /**
     * Cung cấp bean PasswordEncoder sử dụng BCrypt để mã hóa mật khẩu.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Định nghĩa UserDetailsService để lấy thông tin người dùng từ database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Cấu hình AuthenticationProvider sử dụng DaoAuthenticationProvider,
     * lấy dữ liệu người dùng từ UserDetailsService và mã hóa mật khẩu bằng BCrypt.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Cung cấp AuthenticationManager để xử lý xác thực người dùng.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
