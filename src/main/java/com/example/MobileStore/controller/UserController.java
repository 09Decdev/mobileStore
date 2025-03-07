package com.example.MobileStore.controller;

import com.example.MobileStore.dto.LoginDTO;
import com.example.MobileStore.dto.RegisterDTO;
import com.example.MobileStore.exception.InvalidCredentialsException;
import com.example.MobileStore.security.JwtTokenProvider;
import com.example.MobileStore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController // Đánh dấu class này là một REST API controller
@RequestMapping("api/users") // Định nghĩa đường dẫn chung cho tất cả các API trong class này
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired // Tiêm các dependency thông qua constructor
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * API đăng ký người dùng mới
     * @param registerDTO chứa thông tin đăng ký của người dùng
     * @return ResponseEntity chứa thông tin người dùng đã được tạo
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@RequestBody RegisterDTO registerDTO){
        RegisterDTO user = userService.saveUser(registerDTO); // Gọi service để lưu người dùng
        return ResponseEntity.status(HttpStatus.CREATED).body(user); // Trả về HTTP 201 Created
    }

    /**
     * API đăng nhập người dùng
     * @param loginDTO chứa thông tin đăng nhập (username và password)
     * @return Token JWT nếu đăng nhập thành công, hoặc ngoại lệ nếu thất bại
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO){
        try {
            // Xác thực thông tin người dùng bằng AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            // Nếu xác thực thành công, tạo JWT token
            return ResponseEntity.ok(jwtTokenProvider.generateToken(authentication));
        } catch (AuthenticationException e) {
            // Nếu xác thực thất bại, ném ngoại lệ với thông báo lỗi
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
}

//
//@RestController
//@RequestMapping("api/users")
//public class UserController {
//    private final UserService userService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
//        this.userService = userService;
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<RegisterDTO>register(@Valid @RequestBody RegisterDTO registerDTO){
//        RegisterDTO user=userService.saveUser(registerDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String>login(@Valid @RequestBody LoginDTO loginDTO){
//        try {
//            Authentication authentication=authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword())
//            );
//            return ResponseEntity.ok(jwtTokenProvider.generateToken(authentication));
//        }catch (AuthenticationException e){
//            throw new InvalidCredentialsException("Invalid username or password");
//        }
//    }
//}
